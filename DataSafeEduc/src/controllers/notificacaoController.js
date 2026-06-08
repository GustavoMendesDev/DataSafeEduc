var notificacaoModel = require("../models/notificacaoModel");
var slackService = require("../services/slackService");

function normalizarCanalSlack(slackChannelId) {
    return slackService.normalizarCanal(slackChannelId);
}

function montarMensagemSlack(alertaSisu, alertaRegiao, alertaTendencias) {
    var alertasAtivos = [];

    if (alertaSisu === true || alertaSisu === "true") alertasAtivos.push("SISU e notas de corte");
    if (alertaRegiao === true || alertaRegiao === "true") alertasAtivos.push("desempenho por região e concorrência");
    if (alertaTendencias === true || alertaTendencias === "true") alertasAtivos.push("tendências educacionais");

    return ":bell: *DataSafe Educ - Central de Notificações*\n"
        + "A configuração de alertas pedagógicos foi validada com sucesso.\n"
        + "Alertas ativos: " + (alertasAtivos.length > 0 ? alertasAtivos.join(", ") : "nenhum alerta opcional") + ".\n"
        + "As próximas atualizações relevantes serão enviadas neste canal para apoiar o acompanhamento do cliente fictício.";
}

function buscar(req, res) {
    var usuarioId = req.query.usuarioId;

    notificacaoModel.buscar(usuarioId)
        .then(function (resultado) {
            if (resultado.length > 0) {
                res.status(200).json(resultado[0]);
            } else {
                res.status(204).send("Nenhuma configuração encontrada!");
            }
        }).catch(function (erro) {
            console.log(erro);
            console.log("Houve um erro ao buscar a configuração de notificação: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

function salvar(req, res) {
    var slackChannelId = req.body.slackChannelId;
    var alertaSisu = req.body.alertaSisu;
    var alertaRegiao = req.body.alertaRegiao;
    var alertaTendencias = req.body.alertaTendencias;
    var usuarioId = req.body.usuarioId;

    if (slackChannelId == undefined || slackChannelId.trim() == "") {
        res.status(400).send("Informe o canal do Slack.");
        return;
    }

    notificacaoModel.salvar(
        normalizarCanalSlack(slackChannelId),
        alertaSisu,
        alertaRegiao,
        alertaTendencias,
        usuarioId
    ).then(function (resultado) {
        res.json(resultado);
    }).catch(function (erro) {
        console.log(erro);
        console.log("Houve um erro ao salvar a configuração de notificação: ", erro.sqlMessage);
        res.status(500).json(erro.sqlMessage);
    });
}

function testarSlack(req, res) {
    var slackChannelId = req.body.slackChannelId;
    var alertaSisu = req.body.alertaSisu;
    var alertaRegiao = req.body.alertaRegiao;
    var alertaTendencias = req.body.alertaTendencias;

    if (slackChannelId == undefined || slackChannelId.trim() == "") {
        res.status(400).send("Informe o canal do Slack.");
        return;
    }

    slackService.enviarMensagem(
        normalizarCanalSlack(slackChannelId),
        montarMensagemSlack(alertaSisu, alertaRegiao, alertaTendencias)
    ).then(function (respostaSlack) {
        res.status(200).json({
            mensagem: "Mensagem enviada para o Slack.",
            canal: respostaSlack.channel,
            timestamp: respostaSlack.ts
        });
    }).catch(function (erro) {
        console.log("Houve um erro ao enviar mensagem para o Slack: ", erro.message);
        res.status(502).send(erro.message);
    });
}

module.exports = {
    buscar,
    salvar,
    testarSlack
};
