var coordenadorModel = require("../models/coordenadorModel");

function obterNome(req) {
    return req.body.nomeServer || req.body.nome || req.body.email;
}

function listar(req, res) {
    coordenadorModel.listar()
        .then(function (resultado) {
            res.status(200).json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarPorId(req, res) {
    var idCoordenador = Number(req.params.id);

    if (!idCoordenador) {
        res.status(400).send("ID do coordenador inválido!");
        return;
    }

    coordenadorModel.buscarPorId(idCoordenador)
        .then(function (resultado) {
            if (resultado.length == 0) {
                res.status(404).send("Coordenador não encontrado!");
            } else {
                res.status(200).json(resultado[0]);
            }
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function cadastrar(req, res) {
    var nome = obterNome(req);
    var senha = req.body.senhaServer || req.body.senha;
    var confirmarSenha = req.body.confirmarSenhaServer || req.body.confirmarSenha;
    var fkMunicipio = req.body.fkMunicipio;
    var nomeCursinho = req.body.nomeCursinho || req.body.cursinho;
    var fkCursinho = req.body.fkCursinho || req.body.cursinho_id;

    if (nome == undefined || nome == "") {
        res.status(400).send("Nome ou email do coordenador está undefined!");
    } else if (senha == undefined || senha == "") {
        res.status(400).send("Senha do coordenador está undefined!");
    } else if (confirmarSenha != undefined && senha != confirmarSenha) {
        res.status(400).send("As senhas não conferem!");
    } else if (fkMunicipio != undefined && fkMunicipio != "" && !Number(fkMunicipio)) {
        res.status(400).send("Município inválido!");
    } else if (fkCursinho != undefined && fkCursinho != "" && !Number(fkCursinho)) {
        res.status(400).send("Cursinho inválido!");
    } else {
        coordenadorModel.cadastrar(nome, senha, fkMunicipio, nomeCursinho, fkCursinho)
            .then(function (resultado) {
                res.status(201).json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function atualizar(req, res) {
    var idCoordenador = Number(req.params.id);
    var nome = obterNome(req);
    var senha = req.body.senhaServer || req.body.senha;
    var confirmarSenha = req.body.confirmarSenhaServer || req.body.confirmarSenha;
    var fkMunicipio = req.body.fkMunicipio;
    var nomeCursinho = req.body.nomeCursinho || req.body.cursinho;
    var fkCursinho = req.body.fkCursinho || req.body.cursinho_id;

    if (!idCoordenador) {
        res.status(400).send("ID do coordenador inválido!");
    } else if (nome == undefined || nome == "") {
        res.status(400).send("Nome ou email do coordenador está undefined!");
    } else if (confirmarSenha != undefined && senha != confirmarSenha) {
        res.status(400).send("As senhas não conferem!");
    } else if (fkMunicipio != undefined && fkMunicipio != "" && !Number(fkMunicipio)) {
        res.status(400).send("Município inválido!");
    } else if (fkCursinho != undefined && fkCursinho != "" && !Number(fkCursinho)) {
        res.status(400).send("Cursinho inválido!");
    } else {
        coordenadorModel.atualizar(idCoordenador, nome, senha, fkMunicipio, nomeCursinho, fkCursinho)
            .then(function (resultado) {
                if (resultado.affectedRows == 0) {
                    res.status(404).send("Coordenador não encontrado!");
                } else {
                    res.status(200).json(resultado);
                }
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function excluir(req, res) {
    var idCoordenador = Number(req.params.id);

    if (!idCoordenador) {
        res.status(400).send("ID do coordenador inválido!");
    } else {
        coordenadorModel.excluir(idCoordenador)
            .then(function (resultado) {
                if (resultado.affectedRows == 0) {
                    res.status(404).send("Coordenador não encontrado!");
                } else {
                    res.status(200).json(resultado);
                }
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

module.exports = {
    listar,
    buscarPorId,
    cadastrar,
    atualizar,
    excluir
};
