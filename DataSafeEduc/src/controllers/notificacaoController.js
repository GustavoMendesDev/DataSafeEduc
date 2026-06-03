var notificacaoModel = require("../models/notificacaoModel");

function buscar(req, res) {
    notificacaoModel.buscar()
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
    var periodo = req.body.periodo;
    var notificarSistema = req.body.notificarSistema;
    var notificarEmail = req.body.notificarEmail;
    var encerrarSessao = req.body.encerrarSessao;
    var usuarioId = req.body.usuarioId;

    if (periodo == undefined || Number(periodo) <= 0) {
        res.status(400).send("Informe um período válido.");
        return;
    }

    if (slackChannelId == undefined || slackChannelId == "") {
        res.status(400).send("Informe o canal do Slack.");
        return;
    }

    notificacaoModel.salvar(
        slackChannelId,
        periodo,
        notificarSistema,
        notificarEmail,
        encerrarSessao,
        usuarioId
    ).then(function (resultado) {
        res.json(resultado);
    }).catch(function (erro) {
        console.log(erro);
        console.log("Houve um erro ao salvar a configuração de notificação: ", erro.sqlMessage);
        res.status(500).json(erro.sqlMessage);
    });
}

module.exports = {
    buscar,
    salvar
};
