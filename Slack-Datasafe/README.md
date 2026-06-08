# Slack-Datasafe

Integração simples em Java entre o Data Safe e o Slack.

O projeto consome a API do Slack usando Java e envia notificações automáticas de tempos em tempos.

## Funcionalidades

- Enviar mensagem para um canal do Slack.
- Escolher a periodicidade do envio.
- Consultar no banco os usuários sem acesso recente.
- Configurar o limite de dias sem acesso e link do sistema.
- Manter o código simples, com poucas classes.

## Arquivos principais

- `DataSafeSlackApplication.java`: classe principal.
- `NotificacaoConfig.java`: lê as configurações.
- `SlackBot.java`: consome a API do Slack.

## Configuração

O Java lê primeiro as variáveis de ambiente do sistema. Se elas não existirem, ele também tenta ler um arquivo `.env` nos caminhos mais comuns do projeto, incluindo `DataSafeEduc/.env`.

No seu `.env`, deixe pelo menos:

```env
export SLACK_BOT_TOKEN="xoxb-seu-token"
export SLACK_CHANNEL_ID="C0B4W61GC73"
export PERIODICIDADE_MINUTOS="60"
export DIAS_SEM_ACESSO="7"
export LINK_SISTEMA="http://localhost:8080"
export DB_HOST="localhost"
export DB_PORT="3306"
export DB_DATABASE="dataSafe"
export DB_USER="root"
export DB_PASSWORD="sua-senha"
```

Se estiver usando o arquivo `DataSafeEduc/.env`, ele também pode ficar no formato sem `export`, como o Node já usa:

```env
DB_HOST='localhost'
DB_DATABASE='dataSafe'
DB_USER='root'
DB_PASSWORD='12345678'
DB_PORT='3306'
SLACK_BOT_TOKEN='xoxb-seu-token'
SLACK_CHANNEL_ID='C0B4W61GC73'
```

## Rodando

Dentro de `slack`:

```bash
mvn clean package
java -jar target/slack-1.0-SNAPSHOT.jar
```

Se `PERIODICIDADE_MINUTOS` for `60`, o Java envia uma mensagem ao Slack a cada 60 minutos.

## Teste direto da API do Slack

Para enviar uma mensagem de teste sem depender do banco:

```bash
export SLACK_BOT_TOKEN="xoxb-seu-token"
export SLACK_CHANNEL_ID="C0B4W61GC73"

cd Slack-Datasafe/slack
mvn clean package
java -jar target/slack-1.0-SNAPSHOT.jar --teste-slack "Integração Java com Slack funcionando"
```

O bot precisa estar instalado no workspace, ter a permissão `chat:write` e participar do canal informado.

## Como corrigir a conexão no seu projeto

1. Crie ou abra o app em https://api.slack.com/apps.
2. Em **OAuth & Permissions**, adicione o escopo `chat:write` em **Bot Token Scopes**.
3. Instale ou reinstale o app no workspace.
4. Copie o **Bot User OAuth Token**. Ele deve começar com `xoxb-`.
5. Coloque esse token em `SLACK_BOT_TOKEN`.
6. Coloque o ID do canal em `SLACK_CHANNEL_ID`, por exemplo `C0B4W61GC73`.
7. Convide o bot para o canal no Slack.
8. Rode o teste direto:

```bash
cd Slack-Datasafe/slack
mvn clean package
java -jar target/slack-1.0-SNAPSHOT.jar --teste-slack "Integração Java com Slack funcionando"
```

Não coloque token fixo dentro do código Java. Token antigo, token `xoxp`/`xoxe` ou token de workspace errado costuma gerar `invalid_auth`, `not_authed` ou `account_inactive`.
