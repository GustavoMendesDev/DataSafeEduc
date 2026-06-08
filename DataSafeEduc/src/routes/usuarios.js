var express = require("express");
var router = express.Router();

var usuarioController = require("../controllers/usuarioController");

//Recebendo os dados do html e direcionando para a função cadastrar de usuarioController.js
router.post("/cadastrar", function (req, res) {
    usuarioController.cadastrar(req, res);
<<<<<<< HEAD
})
=======
});
>>>>>>> dashboard

router.post("/autenticar", function (req, res) {
    usuarioController.autenticar(req, res);
});

<<<<<<< HEAD
module.exports = router;
=======
router.post("/cadastrarCoodenador", function(req,res){
    usuarioController.cadastrarCoordenador(req,res)
});



router.get("/listar/:nivel", function (req, res) {
    usuarioController.listarPorNivel(req, res);
});

router.post("/cadastrar-completo", function (req, res) {
    usuarioController.cadastrarCompleto(req, res);
});

router.put("/:id", function (req, res) {
    usuarioController.atualizar(req, res);
});

router.delete("/:id", function (req, res) {
    usuarioController.excluir(req, res);
});

module.exports = router;
>>>>>>> dashboard
