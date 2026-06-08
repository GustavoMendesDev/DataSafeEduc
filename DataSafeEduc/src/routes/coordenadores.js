var express = require("express");
var router = express.Router();

var coordenadorController = require("../controllers/coordenadorController");

router.get("/", function (req, res) {
    coordenadorController.listar(req, res);
});

router.get("/:id", function (req, res) {
    coordenadorController.buscarPorId(req, res);
});

router.post("/", function (req, res) {
    coordenadorController.cadastrar(req, res);
});

router.post("/cadastrar", function (req, res) {
    coordenadorController.cadastrar(req, res);
});

router.put("/:id", function (req, res) {
    coordenadorController.atualizar(req, res);
});

router.delete("/:id", function (req, res) {
    coordenadorController.excluir(req, res);
});

module.exports = router;
