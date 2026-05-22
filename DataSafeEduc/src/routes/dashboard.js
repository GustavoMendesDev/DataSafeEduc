var express = require("express");
var router = express.Router();

var dashboardController = require("../controllers/dashboardController");

router.get("/notas-municipais", function (req, res) {
    dashboardController.buscarNotasMunicipais(req, res);
});

router.get("/evolucao-notas", function (req, res) {
    dashboardController.buscarEvolucaoNotas(req, res);
});

router.get("/habilidades-abaixo-media/:sigla", function (req, res) {
    dashboardController.buscarHabilidadesAbaixoMedia(req, res);
});

router.get("/habilidades-acima-media/:sigla", function (req, res) {
    dashboardController.buscarHabilidadesAcimaMedia(req, res);
});

router.get("/notas-habilidades/:sigla", function (req, res) {
    dashboardController.buscarNotasHabilidades(req, res);
});

router.get("/questoes-por-area", function (req, res) {
    dashboardController.buscarQuestoesPorArea(req, res);
});

router.get("/questoes-por-nivel/:sigla?", function (req, res) {
    dashboardController.buscarQuestoesPorNivel(req, res);
});

router.get("/habilidades-frequentes", function (req, res) {
    dashboardController.buscarHabilidadesFrequentes(req, res);
});

router.get("/questoes", function (req, res) {
    dashboardController.buscarQuestoes(req, res);
});

module.exports = router;
