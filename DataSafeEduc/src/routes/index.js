var express = require("express");
var path = require("path");
var router = express.Router();
var path = require("path");

router.get("/", function (req, res) {
const usuario = require('./usuario');
const dashboard = require('./dashboard');
});

router.get("/:pagina", function (req, res, next) {
    var paginas = [
        "index",
        "login",
        "cadastro",
        "analise-desempenho",
        "dash-monitoramento",
        "lista-desempenho",
        "simulados",
        "gerenciamento-professores",
        "gerenciamento-coordenadores",
        "duck-ia" 
    ];

    var pagina = req.params.pagina.replace(".html", "");

    if (!paginas.includes(pagina)) {
        next();
        return;
    }

    res.sendFile(path.join(__dirname, `../../public/views/${pagina}.html`));
});

module.exports = router;
