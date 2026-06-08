var express = require("express");
var router = express.Router();

var professorController = require("../controllers/professorController");

router.get("/", function (req, res) {
    professorController.listar(req, res);
});

router.get("/:idProfessor", function (req, res) {
    professorController.buscarPorId(req, res);
});

router.post("/cadastrar", function (req, res) {
    professorController.cadastrar(req, res);
});

router.put("/:idProfessor", function (req, res) {
    professorController.atualizar(req, res);
});

router.delete("/:idProfessor", function (req, res) {
    professorController.excluir(req, res);
});

module.exports = router;