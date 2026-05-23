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

const LINHA_DOMINIO_PERCENT = 60;

const AREAS_VALIDAS = new Set(['LC', 'CH', 'CN', 'MT']);

function validarArea(sigla) {
    return AREAS_VALIDAS.has(sigla);
}
function serializarResultado(resultado) {
    return {
        labels:       resultado.map(item => `${item.habilidade}`),
        values:       resultado.map(item => Number(item.chanceAcerto)),
        cores:        resultado.map(item => classificarChance(Number(item.chanceAcerto)).cor),
        rotulos:      resultado.map(item => classificarChance(Number(item.chanceAcerto)).rotulo),
        linhaDominio: LINHA_DOMINIO_PERCENT,
        detalhes:     resultado,
        vazio:        resultado.length === 0,   // ← flag explícita para o frontend
    };
}
// ── 4 tiers baseados exclusivamente no parâmetro B da TRI ─────────────────────
function classificarChance(chance) {
    if (chance >= 50) return { rotulo: 'Alta',        cor: '#22c55e' };
    if (chance >= 40) return { rotulo: 'Média',       cor: '#f97316' };
    if (chance >= 30) return { rotulo: 'Baixa',       cor: '#ef4444' };
    return                   { rotulo: 'Muito Baixa', cor: '#7f1d1d' };
}

// ── Helper: serializa resultado do model para o formato do chart ──────────────
function serializarResultado(resultado) {
    return {
        labels:       resultado.map(item => `${item.habilidade}`),
        values:       resultado.map(item => Number(item.chanceAcerto)),
        cores:        resultado.map(item => classificarChance(Number(item.chanceAcerto)).cor),
        rotulos:      resultado.map(item => classificarChance(Number(item.chanceAcerto)).rotulo),
        linhaDominio: LINHA_DOMINIO_PERCENT,
        detalhes:     resultado,
    };
}

function buscarHabilidadesAbaixoMedia(req, res) {
    const { sigla } = req.params;
    if (!validarArea(sigla)) return res.status(400).json({ erro: 'Área inválida.' });

    dashboardModel.buscarHabilidadesAbaixoMedia(sigla)
        .then(resultado => res.status(200).json(serializarResultado(resultado)))
        .catch(erro => {
            console.error('[buscarHabilidadesAbaixoMedia]', erro);
            res.status(500).json({ erro: erro.sqlMessage ?? 'Erro interno.' });
        });
}
function buscarHabilidadesAcimaMedia(req, res) {
    const { sigla } = req.params;
    if (!validarArea(sigla)) return res.status(400).json({ erro: 'Área inválida.' });

    dashboardModel.buscarHabilidadesAcimaMedia(sigla)
        .then(resultado => res.status(200).json(serializarResultado(resultado)))
        .catch(erro => {
            console.error('[buscarHabilidadesAcimaMedia]', erro);
            res.status(500).json({ erro: erro.sqlMessage ?? 'Erro interno.' });
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
