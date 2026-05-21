var coordenadorModel = require("../models/coordenadorModel");

function cadastrarCoordenador(res, res){
    var nomeCursinho = req.body.nomeCursinho;
    var email = req.body.email;
    var senha = req.body.senha;
    var confirmarSenha = req.body.confirmarSenha;

    if(nomeCursinho == undefined){
        res.status(400).send("Nome cursinho undefined!");
    }else if(email == undefined){
        res.status(400).send("Seu email está undefined");
    }else if(senha == undefined){
        res.status(400).send("Sua senha está undefined!");
    }else if(confirmarSenha == undefined){
        res.status(400).send("Digite a senha igual ao campo anterior")
    }else{
         // Passe os valores como parâmetro e vá para o arquivo usuarioModel.js
                coordenadorModel.cadastrar(nomeCursinho, email, senha, confirmarSenha)
                    .then(
                        function (resultado) {
                            res.json(resultado);
                        }
                    ).catch(
                        function (erro) {
                            console.log(erro);
                            console.log(
                                "\nHouve um erro ao realizar o cadastro! Erro: ",
                                erro.sqlMessage
                            );
                            res.status(500).json(erro.sqlMessage);
                        }
                    );
    }
}

module.exports = {
    cadastrar
}