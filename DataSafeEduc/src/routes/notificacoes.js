var express = require("express");
var router = express.Router();

var notificacaoController = require("../controllers/notificacaoController");

router.get("/configuracao", function (req, res) {
    notificacaoController.buscar(req, res);
});

router.post("/configuracao", function (req, res) {
    notificacaoController.salvar(req, res);
});

router.post("/teste-slack", function (req, res) {
    notificacaoController.testarSlack(req, res);
});

module.exports = router;
