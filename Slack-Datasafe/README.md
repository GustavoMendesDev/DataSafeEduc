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

## Variáveis de ambiente

```bash
export SLACK_BOT_TOKEN="xoxb-seu-token"
export SLACK_CHANNEL_ID="#equipe-datasafe"
export PERIODICIDADE_MINUTOS="60"
export DIAS_SEM_ACESSO="7"
export LINK_SISTEMA="http://localhost:8080"
export DB_HOST="localhost"
export DB_PORT="3306"
export DB_DATABASE="datasafeeduc"
export DB_USER="root"
export DB_PASSWORD="sua-senha"
```

## Rodando

Dentro de `slack`:

```bash
mvn clean package
java -jar target/slack-1.0-SNAPSHOT.jar
```

Se `PERIODICIDADE_MINUTOS` for `60`, o Java envia uma mensagem ao Slack a cada 60 minutos.
