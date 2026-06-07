var notificacaoModel = require("../models/notificacaoModel");

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
        slackChannelId.trim(),
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

module.exports = {
    buscar,
    salvar
};
