var database = require("../database/config");

function escapar(valor) {
    return String(valor).replace(/\\/g, "\\\\").replace(/'/g, "\\'");
}

function valorSql(valor) {
    if (valor == undefined || valor == "") {
        return "NULL";
    }

    return `'${escapar(valor)}'`;
}

function booleanSql(valor) {
    return valor === true || valor === "true" || valor === 1 || valor === "1" ? 1 : 0;
}

function usuarioIdSql(usuarioId) {
    var numeroUsuarioId = Number(usuarioId);

    if (usuarioId == undefined || usuarioId == "" || !Number.isFinite(numeroUsuarioId)) {
        return "NULL";
    }

    return numeroUsuarioId;
}

function garantirTabela() {
    var instrucaoSql = `
        CREATE TABLE IF NOT EXISTS controleNotificacao (
            id INT NOT NULL AUTO_INCREMENT,
            slack_channel_id VARCHAR(100) NOT NULL,
            periodo INT NOT NULL,
            receberNotificacao VARCHAR(3) NOT NULL,
            tipoNotificacao TINYINT(1) NOT NULL,
            notificarSistema TINYINT(1) NOT NULL,
            notificarEmail TINYINT(1) NOT NULL,
            encerrarSessao TINYINT(1) NOT NULL,
            ativo TINYINT(1) NOT NULL DEFAULT 1,
            usuario_id INT NULL,
            PRIMARY KEY (id)
        );
    `;

    return database.executar(instrucaoSql);
}

function buscar(usuarioId) {
    var filtroUsuario = usuarioIdSql(usuarioId);
    var whereUsuario = filtroUsuario === "NULL" ? "" : `AND usuario_id = ${filtroUsuario}`;

    var instrucaoSql = `
        SELECT
            id,
            slack_channel_id AS slackChannelId,
            notificarSistema AS alertaSisu,
            notificarEmail AS alertaRegiao,
            encerrarSessao AS alertaTendencias,
            ativo,
            usuario_id AS usuarioId
        FROM controleNotificacao
        WHERE ativo = 1
          ${whereUsuario}
        ORDER BY id DESC
        LIMIT 1;
    `;

    return garantirTabela().then(function () {
        console.log("Executando a instrução SQL: \n" + instrucaoSql);
        return database.executar(instrucaoSql);
    });
}

function salvar(slackChannelId, alertaSisu, alertaRegiao, alertaTendencias, usuarioId) {
    var algumAlertaAtivo = booleanSql(alertaSisu) || booleanSql(alertaRegiao) || booleanSql(alertaTendencias);

    var instrucaoSql = `
        INSERT INTO controleNotificacao (
            slack_channel_id,
            periodo,
            receberNotificacao,
            tipoNotificacao,
            notificarSistema,
            notificarEmail,
            encerrarSessao,
            ativo,
            usuario_id
        ) VALUES (
            ${valorSql(slackChannelId)},
            43200,
            ${algumAlertaAtivo ? "'Sim'" : "'Não'"},
            ${algumAlertaAtivo},
            ${booleanSql(alertaSisu)},
            ${booleanSql(alertaRegiao)},
            ${booleanSql(alertaTendencias)},
            1,
            ${usuarioIdSql(usuarioId)}
        );
    `;

    return garantirTabela().then(function () {
        console.log("Executando a instrução SQL: \n" + instrucaoSql);
        return database.executar(instrucaoSql);
    });
}

module.exports = {
    buscar,
    salvar
};
