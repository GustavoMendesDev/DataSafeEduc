var express = require("express");
var router = express.Router();

var simuladoController = require("../controllers/simuladoController");

router.get("/", function (req, res) {
    simuladoController.listar(req, res);
});

router.get("/opcoes/habilidades/:sigla?", function (req, res) {
    simuladoController.listarHabilidades(req, res);
});

router.get("/opcoes/questoes", function (req, res) {
    simuladoController.listarQuestoesDisponiveis(req, res);
});

router.get("/:id", function (req, res) {
    simuladoController.detalhar(req, res);
});

router.post("/criar", function (req, res) {
    simuladoController.criar(req, res);
});

router.put("/:id", function (req, res) {
    simuladoController.atualizar(req, res);
});

router.delete("/:id", function (req, res) {
    simuladoController.excluir(req, res);
});

module.exports = router;
