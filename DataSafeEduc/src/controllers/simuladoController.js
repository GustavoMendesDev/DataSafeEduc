var simuladoModel = require("../models/simuladoModel");

var areasValidas = ["LC", "MT", "CN", "CH"];
var dificuldadesValidas = ["Fácil", "Médio", "Difícil"];

function formatarHabilidade(numero) {
    var texto = String(numero);
    return texto.toUpperCase().startsWith("H") ? texto : `H${texto}`;
}

function listar(req, res) {
    simuladoModel.listar()
        .then(function (resultado) {
            res.status(200).json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function detalhar(req, res) {
    var idSimulado = Number(req.params.id);

    if (!idSimulado) {
        res.status(400).send("ID do simulado inválido!");
        return;
    }

    simuladoModel.detalhar(idSimulado)
        .then(function (resultado) {
            if (resultado.length > 0) {
                var primeiraLinha = resultado[0];
                res.status(200).json({
                    id: primeiraLinha.id,
                    nomeSimulado: primeiraLinha.nomeSimulado,
                    quantidadeQuestoes: primeiraLinha.quantidadeQuestoes,
                    area: primeiraLinha.area,
                    sigla: primeiraLinha.sigla,
                    dificuldade: primeiraLinha.dificuldade,
                    habilidades: [...new Set(resultado.map(function (item) { return formatarHabilidade(item.habilidade); }))],
                    indicadores: {
                        questoes: resultado.length,
                        parametroAMedio: media(resultado, "parametroA"),
                        parametroBMedio: media(resultado, "parametroB"),
                        qtdHabilidades: [...new Set(resultado.map(function (item) { return item.habilidade; }))].length
                    },
                    questoes: resultado
                });
            } else {
                res.status(204).send("Nenhum simulado encontrado!");
            }
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function criar(req, res) {
    var nomeSimulado = req.body.nomeSimulado;
    var quantidadeQuestoes = Number(req.body.quantidadeQuestoes);
    var fkUsuario = Number(req.body.fkUsuario || req.body.idUsuario);
    var sigla = req.body.sigla;
    var dificuldade = req.body.dificuldade;
    var habilidades = req.body.habilidades || [];

    if (nomeSimulado == undefined || nomeSimulado == "") {
        res.status(400).send("Nome do simulado está undefined!");
    } else if (!quantidadeQuestoes) {
        res.status(400).send("Quantidade de questões inválida!");
    } else if (!fkUsuario) {
        res.status(400).send("Usuário do simulado está undefined!");
    } else if (sigla && !areasValidas.includes(sigla)) {
        res.status(400).send("Área inválida!");
    } else if (dificuldade && !dificuldadesValidas.includes(dificuldade)) {
        res.status(400).send("Dificuldade inválida!");
    } else {
        simuladoModel.buscarQuestoesParaSimulado(sigla, dificuldade, habilidades, quantidadeQuestoes)
            .then(function (questoes) {
                if (questoes.length == 0) {
                    res.status(204).send("Nenhuma questão encontrada para os filtros informados!");
                    return Promise.reject({ finalizar: true });
                }

                return simuladoModel.criar(nomeSimulado, questoes.length, fkUsuario)
                    .then(function (resultadoCriacao) {
                        return simuladoModel.vincularQuestoes(resultadoCriacao.insertId, questoes)
                            .then(function () {
                                res.status(201).json({
                                    id: resultadoCriacao.insertId,
                                    nomeSimulado: nomeSimulado,
                                    quantidadeQuestoes: questoes.length,
                                    questoes: questoes
                                });
                            });
                    });
            }).catch(function (erro) {
                if (erro && erro.finalizar) return;
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function excluir(req, res) {
    var idSimulado = Number(req.params.id);

    if (!idSimulado) {
        res.status(400).send("ID do simulado inválido!");
        return;
    }

    simuladoModel.excluirQuestoes(idSimulado)
        .then(function () {
            return simuladoModel.excluir(idSimulado);
        }).then(function (resultado) {
            res.status(200).json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function listarHabilidades(req, res) {
    var sigla = req.params.sigla;

    if (sigla && !areasValidas.includes(sigla)) {
        res.status(400).send("Área inválida!");
        return;
    }

    simuladoModel.listarHabilidades(sigla)
        .then(function (resultado) {
            res.status(200).json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function media(lista, campo) {
    if (lista.length == 0) return 0;
    var soma = lista.reduce(function (total, item) {
        return total + Number(item[campo] || 0);
    }, 0);

    return Number((soma / lista.length).toFixed(2));
}

module.exports = {
    listar,
    detalhar,
    criar,
    excluir,
    listarHabilidades
};
