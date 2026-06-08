var dashboardModel = require("../models/dashboardModel");

// ── CONSTANTES ────────────────────────────────────────────────────────────────
const LINHA_DOMINIO_PERCENT = 60;
const AREAS_VALIDAS = new Set(['LC', 'CH', 'CN', 'MT']);

const NIVEL_COGNITIVO = {
    LC: {
        H01:1, H02:1, H03:1, H04:1,
        H05:2, H06:2, H07:2, H08:2, H09:2, H10:2, H11:2,
        H12:3, H13:3, H14:3, H15:3, H16:3, H17:3,
        H18:4, H19:4, H20:4, H21:4, H22:4, H23:4, H24:4,
        H25:5, H26:5, H27:5, H28:5, H29:5, H30:5,
    },
    CH: {
        H01:1, H02:1, H03:1, H04:1, H05:1,
        H06:2, H07:2, H08:2, H09:2, H10:2,
        H11:3, H12:3, H13:3, H14:3, H15:3, H16:3, H17:3, H18:3, H19:3, H20:3,
        H21:4, H22:4, H23:4, H24:4, H25:4,
        H26:5, H27:5, H28:5, H29:5, H30:5,
    },
    CN: {
        H01:1, H02:1, H03:1, H04:1,
        H05:2, H06:2, H07:2, H08:2, H09:2, H10:2, H11:2,
        H12:3, H13:3, H14:3, H15:3, H16:3, H17:3, H18:3, H19:3,
        H20:4, H21:4, H22:4, H23:4, H24:4, H25:4, H26:4, H27:4,
        H28:5, H29:5, H30:5,
    },
    MT: {
        H01:1, H02:1, H03:1, H04:1, H05:1,
        H06:2, H07:2, H08:2, H09:2, H10:2, H11:2, H12:2, H13:2, H14:2, H15:2,
        H16:3, H17:3, H18:3, H19:3, H20:3, H21:3, H22:3, H23:3,
        H24:4, H25:4, H26:4, H27:4,
        H28:5, H29:5, H30:5,
    },
};

const COR_NIVEL = {
    1: '#4BB8FA',
    2: '#3b82f6',
    3: '#a855f7',
    4: '#f97316',
    5: '#ef4444',
};

const COR_NIVEL_PILHA = {
    1: '#4BB8FA',
    2: '#3b82f6',
    3: '#a855f7',
    4: '#FFC94D',
    5: '#ef4444',
};

// ── HELPERS ───────────────────────────────────────────────────────────────────
function validarArea(sigla, permitirTodos) {
    if (permitirTodos && sigla === 'todos') return true;
    return AREAS_VALIDAS.has(sigla);
}

function normalizarChave(habilidade) {
    const s = String(habilidade);
    return s.startsWith('H') ? s : `H${s.padStart(2, '0')}`;
}

function corPorNivel(sigla, habilidade) {
    const chave = normalizarChave(habilidade); // ← era: `H${habilidade}` sem pad, gerava H1, H2...
    const nivel = NIVEL_COGNITIVO[sigla]?.[chave] ?? 1;
    return COR_NIVEL[nivel];
}

function classificarChance(chance) {
    if (chance >= 50) return { rotulo: 'Alta',        cor: '#22c55e' };
    if (chance >= 40) return { rotulo: 'Média',       cor: '#f97316' };
    if (chance >= 30) return { rotulo: 'Baixa',       cor: '#ef4444' };
    return                   { rotulo: 'Muito Baixa', cor: '#7f1d1d' };
}

function serializarResultado(resultado) {
    return {
        labels:       resultado.map(item => `${item.habilidade}`),
        values:       resultado.map(item => Number(item.chanceAcerto)),
        cores:        resultado.map(item => classificarChance(Number(item.chanceAcerto)).cor),
        rotulos:      resultado.map(item => classificarChance(Number(item.chanceAcerto)).rotulo),
        linhaDominio: LINHA_DOMINIO_PERCENT,
        detalhes:     resultado,
        vazio:        resultado.length === 0,
    };
}
// ── CONTROLLERS ───────────────────────────────────────────────────────────────
function buscarNotasMunicipais(req, res) {
    dashboardModel.buscarNotasMunicipais()
        .then(resultado => {
            if (resultado.length > 0) {
                res.status(200).json(resultado[0]);
            } else {
                res.status(204).send("Nenhuma nota municipal encontrada!");
            }
        })
        .catch(erro => {
            console.error('[buscarNotasMunicipais]', erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarEvolucaoNotas(req, res) {
    dashboardModel.buscarEvolucaoNotas()
        .then(resultado => {
            if (!resultado.length) {
                return res.status(204).send("Nenhuma nota encontrada!");
            }
            res.status(200).json({
                labels: resultado.map(item => String(item.anoExame)), // ✅ era item.municipio
                datasets: [
                    { label: "Matemática", data: resultado.map(item => Number(item.matematica).toFixed(2)) },
                    { label: "Natureza",   data: resultado.map(item => Number(item.cienciasDaNatureza).toFixed(2)) },
                    { label: "Humanas",    data: resultado.map(item => Number(item.cienciasHumanas).toFixed(2)) },
                    { label: "Linguagens", data: resultado.map(item => Number(item.codigosELinguagens).toFixed(2)) },
                ],
            });
        })
        .catch(erro => {
            console.error('[buscarEvolucaoNotas]', erro);
            res.status(500).json(erro.sqlMessage);
        });
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

function buscarHabilidadesMaiorImpactoNota(req, res) {
    const { sigla } = req.params;
    if (!validarArea(sigla)) return res.status(400).json({ erro: 'Área inválida.' });  // ← corrigido: usava AREAS_VALIDAS.has diretamente

    dashboardModel.buscarHabilidadesMaiorImpactoNota(sigla)
        .then(resultado => {
            res.status(200).json({
                labels:       resultado.map(item => `${item.habilidade}`),
                values:       resultado.map(item => Number(item.impactoNota)),
                cores:        resultado.map(item => corPorNivel(sigla, item.habilidade)),
                rotulos:      resultado.map(item =>
                    `Discriminação: ${item.discriminacao} | Dificuldade: ${item.dificuldade}`
                ),
                linhaDominio: null,
                detalhes:     resultado,
                vazio:        resultado.length === 0,
            });
        })
        .catch(erro => {
            console.error('[buscarHabilidadesMaiorImpactoNota]', erro);
            res.status(500).json({ erro: erro.sqlMessage ?? 'Erro interno.' });
        });
}

function buscarQuestoesPorArea(req, res) {
    dashboardModel.buscarQuestoesPorArea()
        .then(resultado => {
            res.status(200).json({
                labels:   resultado.map(item => item.area),
                values:   resultado.map(item => Number(item.quantidade)),
                detalhes: resultado,
            });
        })
        .catch(erro => {
            console.error('[buscarQuestoesPorArea]', erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarQuestoesPorNivel(req, res) {
    const sigla = req.params.sigla || 'todos';
    if (!validarArea(sigla, true)) return res.status(400).send('Área inválida!');

    const COR_NIVEL_PILHA = {
        1: '#4BB8FA',
        2: '#3b82f6',
        3: '#a855f7',
        4: '#FFC94D',
        5: '#ef4444',
    };

    dashboardModel.buscarQuestoesPorNivel(sigla)
        .then(resultado => {
            const anos   = ['2020', '2021', '2022', '2023', '2024'];
            const niveis = [1, 2, 3, 4, 5];

            // ✅ NOVO: contadores para diagnóstico
            let semNivel = 0;

            const linhasComNivel = resultado.map(item => {
                const areaSigla = sigla !== 'todos' ? sigla : item.area;
                const chave = String(item.habilidade).startsWith('H')
                    ? item.habilidade
                    : `H${String(item.habilidade).padStart(2, '0')}`;

                const nivelEncontrado = NIVEL_COGNITIVO[areaSigla]?.[chave];

                // ✅ NOVO: loga os casos onde o nível não foi encontrado
                if (nivelEncontrado === undefined) {
                    semNivel++;
                    console.warn(
                        `[buscarQuestoesPorNivel] Nível não encontrado: area="${areaSigla}" chave="${chave}" habilidade="${item.habilidade}"`
                    );
                }

                const nivel = nivelEncontrado ?? 1;
                return { ano: String(item.anoExame), nivel, quantidade: Number(item.quantidade) };
            });

            // ✅ NOVO: resumo no log
            console.log(`[buscarQuestoesPorNivel] Total linhas: ${resultado.length} | Sem nível mapeado: ${semNivel}`);

            res.status(200).json({
                labels: anos,
                datasets: niveis.map(nivel => ({
                    label:  `Nível ${nivel}`,
                    color:  COR_NIVEL_PILHA[nivel],
                    values: anos.map(ano =>
                        linhasComNivel
                            .filter(l => l.ano === ano && l.nivel === nivel)
                            .reduce((acc, l) => acc + l.quantidade, 0)
                    ),
                })),
            });
        })
        .catch(erro => {
            console.error('[buscarQuestoesPorNivel]', erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarHabilidadesFrequentes(req, res) {
    dashboardModel.buscarHabilidadesFrequentes()
        .then(resultado => res.status(200).json(resultado))
        .catch(erro => {
            console.error('[buscarHabilidadesFrequentes]', erro);
            res.status(500).json(erro.sqlMessage);
        });
}

function buscarQuestoes(req, res) {
    dashboardModel.buscarQuestoes()
        .then(resultado => res.status(200).json(resultado))
        .catch(erro => {
            console.error('[buscarQuestoes]', erro);
            res.status(500).json(erro.sqlMessage);
        });
}

// ── EXPORTS ───────────────────────────────────────────────────────────────────
module.exports = {
    buscarNotasMunicipais,
    buscarEvolucaoNotas,
    buscarHabilidadesAbaixoMedia,
    buscarHabilidadesAcimaMedia,
    buscarHabilidadesMaiorImpactoNota,
    buscarQuestoesPorArea,
    buscarQuestoesPorNivel,
    buscarHabilidadesFrequentes,
    buscarQuestoes,
};