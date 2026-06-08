const fs = require("fs");
const path = require("path");
const { spawn } = require("child_process");

const raizWeb = path.resolve(__dirname, "..");
const raizProjeto = path.resolve(raizWeb, "..");
const jarSlack = path.join(raizProjeto, "Slack-Datasafe", "slack", "target", "slack-1.0-SNAPSHOT.jar");

function carregarEnv() {
  const caminhoEnv = path.join(raizWeb, ".env");

  if (!fs.existsSync(caminhoEnv)) {
    return;
  }

  const linhas = fs.readFileSync(caminhoEnv, "utf8").split(/\r?\n/);

  linhas.forEach((linha) => {
    const conteudo = linha.trim();

    if (!conteudo || conteudo.startsWith("#") || !conteudo.includes("=")) {
      return;
    }

    const indiceSeparador = conteudo.indexOf("=");
    const chave = conteudo.slice(0, indiceSeparador).trim();
    let valor = conteudo.slice(indiceSeparador + 1).trim();

    if (
      (valor.startsWith("'") && valor.endsWith("'")) ||
      (valor.startsWith('"') && valor.endsWith('"'))
    ) {
      valor = valor.slice(1, -1);
    }

    process.env[chave] = process.env[chave] || valor;
  });
}

function prefixarSaida(prefixo, stream) {
  stream.on("data", (dados) => {
    dados
      .toString()
      .split(/\r?\n/)
      .filter(Boolean)
      .forEach((linha) => console.log(`[${prefixo}] ${linha}`));
  });
}

function iniciarProcesso(nome, comando, argumentos, diretorio) {
  const processo = spawn(comando, argumentos, {
    cwd: diretorio,
    env: process.env,
    stdio: ["ignore", "pipe", "pipe"],
  });

  prefixarSaida(nome, processo.stdout);
  prefixarSaida(nome, processo.stderr);

  processo.on("exit", (codigo, sinal) => {
    if (encerrando) {
      return;
    }

    console.log(`[start] ${nome} encerrou com código ${codigo ?? "n/a"} e sinal ${sinal ?? "n/a"}.`);
    encerrarTodos(codigo || 1);
  });

  return processo;
}

function encerrarTodos(codigo = 0) {
  encerrando = true;

  processos.forEach((processo) => {
    if (!processo.killed) {
      processo.kill("SIGTERM");
    }
  });

  setTimeout(() => process.exit(codigo), 300);
}

let encerrando = false;
const processos = [];

carregarEnv();

if (!fs.existsSync(jarSlack)) {
  console.error(`[start] JAR do Slack não encontrado em: ${jarSlack}`);
  console.error("[start] Rode: cd ../Slack-Datasafe/slack && mvn package");
  process.exit(1);
}

processos.push(iniciarProcesso("slack", "java", ["-jar", jarSlack], raizProjeto));
processos.push(iniciarProcesso("web", "node", ["app.js"], raizWeb));

process.on("SIGINT", () => encerrarTodos(0));
process.on("SIGTERM", () => encerrarTodos(0));
