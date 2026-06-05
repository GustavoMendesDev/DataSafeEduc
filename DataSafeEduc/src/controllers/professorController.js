var professorModel = require("../models/professorModel");

function listar(req, res) {
    var fkCursinho = req.query.fkCursinho || req.headers.fkcursinho;

    if (fkCursinho == undefined || fkCursinho == "" || !Number(fkCursinho)) {
        res.status(400).send("fkCursinho do usuário está undefined!");
        return;
    }

    professorModel.listar(fkCursinho)
        .then(function (resultado) {
            if (resultado.length > 0) {
                res.status(200).json(resultado);
            } else {
                res.status(204).send("Nenhum resultado encontrado!");
            }
        }).catch(function (erro) {
            console.log(erro);
            console.log("Houve um erro ao buscar os professores: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarPorId(req, res) {
    var idProfessor = req.params.idProfessor;
    var fkCursinho = req.query.fkCursinho || req.headers.fkcursinho;

    if (fkCursinho == undefined || fkCursinho == "" || !Number(fkCursinho)) {
        res.status(400).send("fkCursinho do usuário está undefined!");
        return;
    }

    professorModel.buscarPorId(fkCursinho, idProfessor)
        .then(function (resultado) {
            if (resultado.length > 0) {
                res.status(200).json(resultado[0]);
            } else {
                res.status(204).send("Nenhum resultado encontrado!");
            }
        }).catch(function (erro) {
            console.log(erro);
            console.log("Houve um erro ao buscar o professor: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

function cadastrar(req, res) {
    // Recebendo os dados enviados pelo fetch do HTML
    var email = req.body.email; // O HTML manda 'email', a model trata como 'nome'
    var senha = req.body.senha;
    // Capturando a fkCursinho do usuário que está logado/cadastrando
    var fkCursinho = req.body.fkCursinho || req.headers.fkcursinho; 

    if (email == undefined) {
        res.status(400).send("Seu email está undefined!");
    } else if (senha == undefined) {
        res.status(400).send("Sua senha está undefined!");
    } else if (fkCursinho == undefined) {
        res.status(400).send("A fkCursinho está undefined!");
    } else {
        // Enviando APENAS os 3 parâmetros necessários
        professorModel.cadastrar(email, senha, fkCursinho)
            .then(function (resultado) {
                res.json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
   
}

function atualizar(req, res) {
    var idProfessor = req.params.idProfessor;
    var email = req.body.email;
    var senha = req.body.senha;

    var fkMunicipio = undefined;
    var nomeCursinho = undefined;
    var fkCursinho = undefined;

    if (email == undefined) {
        res.status(400).send("Seu email está undefined!");
    } else {
        professorModel.atualizar(idProfessor, email, senha, fkMunicipio, nomeCursinho, fkCursinho)
            .then(function (resultado) {
                res.json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                console.log("\nHouve um erro ao atualizar o professor! Erro: ", erro.sqlMessage);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function excluir(req, res) {
    var idProfessor = req.params.idProfessor;

    professorModel.excluir(idProfessor)
        .then(function (resultado) {
            res.json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            console.log("Houve um erro ao deletar o professor: ", erro.sqlMessage);
            res.status(500).json(erro.sqlMessage);
        });
}

module.exports = {
    listar,
    buscarPorId,
    cadastrar,
    atualizar,
    excluir
};
