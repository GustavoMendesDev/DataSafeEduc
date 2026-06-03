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

function buscar() {
    var instrucaoSql = `
        SELECT
            id,
            slack_channel_id AS slackChannelId,
            periodo,
            notificarSistema,
            notificarEmail,
            encerrarSessao,
            ativo,
            usuario_id AS usuarioId
        FROM controleNotificacao
        WHERE ativo = 1
        ORDER BY id DESC
        LIMIT 1;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function salvar(slackChannelId, periodo, notificarSistema, notificarEmail, encerrarSessao, usuarioId) {
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
            ${Number(periodo)},
            ${booleanSql(notificarSistema) || booleanSql(notificarEmail) ? "'Sim'" : "'Não'"},
            ${booleanSql(notificarSistema)},
            ${booleanSql(notificarSistema)},
            ${booleanSql(notificarEmail)},
            ${booleanSql(encerrarSessao)},
            1,
            ${usuarioId == undefined || usuarioId == "" ? "NULL" : Number(usuarioId)}
        );
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

module.exports = {
    buscar,
    salvar
};
