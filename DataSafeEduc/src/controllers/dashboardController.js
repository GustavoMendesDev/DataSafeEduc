var dashboardModel = require("../models/dashboardModel");

var areasValidas = ["LC", "MT", "CN", "CH"];
var coresNiveis = {
    "Fácil": "#22c55e",
    "Médio": "#3b82f6",
    "Difícil": "#ef4444"
};

function validarArea(sigla, permitirTodos) {
    if (permitirTodos && sigla == "todos") return true;
    return areasValidas.includes(sigla);
}

function formatarHabilidade(numero) {
    var texto = String(numero);
    return texto.toUpperCase().startsWith("H") ? texto : `H${texto}`;
}

function buscarNotasMunicipais(req, res) {
    dashboardModel.buscarNotasMunicipais()
        .then(function (resultado) {
            if (resultado.length > 0) {
                res.status(200).json(resultado[0]);
            } else {
                res.status(204).send("Nenhuma nota municipal encontrada!");
            }
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarEvolucaoNotas(req, res) {
    dashboardModel.buscarEvolucaoNotas()
        .then(function (resultado) {
            if (resultado.length > 0) {
                res.status(200).json({
                    labels: resultado.map(function (item) { return item.municipio || `Registro ${item.id}`; }),
                    datasets: [
                        { label: "Matemática", data: resultado.map(function (item) { return Number(item.matematica); }) },
                        { label: "Natureza", data: resultado.map(function (item) { return Number(item.cienciasDaNatureza); }) },
                        { label: "Humanas", data: resultado.map(function (item) { return Number(item.cienciasHumanas); }) },
                        { label: "Linguagens", data: resultado.map(function (item) { return Number(item.codigosELinguagens); }) }
                    ]
                });
            } else {
                res.status(204).send("Nenhuma nota municipal encontrada!");
            }
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarHabilidadesAbaixoMedia(req, res) {
    var sigla = req.params.sigla;

    if (!validarArea(sigla)) {
        res.status(400).send("Área inválida!");
        return;
    }

    dashboardModel.buscarHabilidadesAbaixoMedia(sigla)
        .then(function (resultado) {
            res.status(200).json({
                labels: resultado.map(function (item) { return formatarHabilidade(item.habilidade); }),
                values: resultado.map(function (item) { return Number(item.quantidade); }),
                detalhes: resultado
            });
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarHabilidadesAcimaMedia(req, res) {
    var sigla = req.params.sigla;

    if (!validarArea(sigla)) {
        res.status(400).send("Área inválida!");
        return;
    }

    dashboardModel.buscarHabilidadesAcimaMedia(sigla)
        .then(function (resultado) {
            res.status(200).json({
                labels: resultado.map(function (item) { return formatarHabilidade(item.habilidade); }),
                values: resultado.map(function (item) { return Number(item.quantidade); }),
                detalhes: resultado
            });
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarNotasHabilidades(req, res) {
    var sigla = req.params.sigla;

    if (!validarArea(sigla)) {
        res.status(400).send("Área inválida!");
        return;
    }

    dashboardModel.buscarNotasHabilidades(sigla)
        .then(function (resultado) {
            res.status(200).json({
                labels: resultado.map(function (item) { return formatarHabilidade(item.habilidade); }),
                values: resultado.map(function (item) { return Number(item.notaMedia); }),
                detalhes: resultado
            });
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarQuestoesPorArea(req, res) {
    dashboardModel.buscarQuestoesPorArea()
        .then(function (resultado) {
            res.status(200).json({
                labels: resultado.map(function (item) { return item.area; }),
                values: resultado.map(function (item) { return Number(item.quantidade); }),
                detalhes: resultado
            });
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarQuestoesPorNivel(req, res) {
    var sigla = req.params.sigla || "todos";

    if (!validarArea(sigla, true)) {
        res.status(400).send("Área inválida!");
        return;
    }

    dashboardModel.buscarQuestoesPorNivel(sigla)
        .then(function (resultado) {
            var anos = [...new Set(resultado.map(function (item) { return String(item.anoExame); }))];
            var niveis = [...new Set(resultado.map(function (item) { return item.nivel; }))];

            res.status(200).json({
                labels: anos,
                datasets: niveis.map(function (nivel) {
                    return {
                        label: nivel,
                        color: coresNiveis[nivel] || "#314595",
                        values: anos.map(function (ano) {
                            var item = resultado.find(function (linha) {
                                return String(linha.anoExame) == ano && linha.nivel == nivel;
                            });
                            return item ? Number(item.quantidade) : 0;
                        })
                    };
                })
            });
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarHabilidadesFrequentes(req, res) {
    dashboardModel.buscarHabilidadesFrequentes()
        .then(function (resultado) {
            res.status(200).json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarQuestoes(req, res) {
    dashboardModel.buscarQuestoes()
        .then(function (resultado) {
            res.status(200).json(resultado);
        }).catch(function (erro) {
            console.log(erro);
            res.status(500).json(erro.sqlMessage);
        });
}

module.exports = {
    buscarNotasMunicipais,
    buscarEvolucaoNotas,
    buscarHabilidadesAbaixoMedia,
    buscarHabilidadesAcimaMedia,
    buscarNotasHabilidades,
    buscarQuestoesPorArea,
    buscarQuestoesPorNivel,
    buscarHabilidadesFrequentes,
    buscarQuestoes
};
