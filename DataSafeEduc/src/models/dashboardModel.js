var database = require("../database/config");
function buscarNotasMunicipais() {
    var instrucaoSql = `
        SELECT
            matematica,
            codigosELinguagens,
            cienciasDaNatureza,
            cienciasHumanas
        FROM notaMunicipal
        WHERE anoExame = 2024 AND id != 1
        LIMIT 1;
    `;
    return database.executar(instrucaoSql);
}
function buscarEvolucaoNotas() {
    var instrucaoSql = `
        SELECT
            anoExame,
            AVG(matematica)         AS matematica,
            AVG(codigosELinguagens) AS codigosELinguagens,
            AVG(cienciasDaNatureza) AS cienciasDaNatureza,
            AVG(cienciasHumanas)    AS cienciasHumanas
        FROM notaMunicipal
        WHERE anoExame BETWEEN 2020 AND 2024
          AND id != 1
        GROUP BY anoExame
        ORDER BY anoExame ASC;
    `;
    return database.executar(instrucaoSql);
}

function buscarHabilidadesAbaixoMedia(sigla) {
    const instrucaoSql = `
        SELECT
            ac.sigla                                        AS area,
            h.numero                                        AS habilidade,
            COUNT(q.codigoItem)                             AS quantidade,
            ROUND(AVG(pt.parametroB), 3)                   AS mediaBParam,
            ROUND(100 / (1 + EXP(AVG(pt.parametroB))), 1) AS chanceAcerto
        FROM questao q
        JOIN habilidade       h  ON q.fkHabilidade      = h.id
        JOIN areaConhecimento ac ON h.fkAreaConhecimento = ac.id
        JOIN parametroTri     pt ON q.fkParametroTri    = pt.id
        WHERE ac.sigla      = '${sigla}'
          AND pt.parametroB IS NOT NULL
          AND q.anoExame    = 2024
        GROUP BY ac.sigla, h.numero
        HAVING chanceAcerto < 30
        ORDER BY chanceAcerto ASC
        LIMIT 8;
    `;
    console.log("SQL buscarHabilidadesAbaixoMedia:\n", instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarHabilidadesAcimaMedia(sigla) {
    const instrucaoSql = `
        SELECT
            ac.sigla                                        AS area,
            h.numero                                        AS habilidade,
            COUNT(q.codigoItem)                             AS quantidade,
            ROUND(AVG(pt.parametroB), 3)                   AS mediaBParam,
            ROUND(100 / (1 + EXP(AVG(pt.parametroB))), 1) AS chanceAcerto
        FROM questao q
        JOIN habilidade       h  ON q.fkHabilidade      = h.id
        JOIN areaConhecimento ac ON h.fkAreaConhecimento = ac.id
        JOIN parametroTri     pt ON q.fkParametroTri    = pt.id
        WHERE ac.sigla      = '${sigla}'
          AND pt.parametroB IS NOT NULL
          AND q.anoExame    = 2024
        GROUP BY ac.sigla, h.numero
        HAVING mediaBParam < 1.0
        ORDER BY mediaBParam ASC
        LIMIT 8;
    `;
    const instrucaoFallback = `
        SELECT
            ac.sigla                                        AS area,
            h.numero                                        AS habilidade,
            COUNT(q.codigoItem)                             AS quantidade,
            ROUND(AVG(pt.parametroB), 3)                   AS mediaBParam,
            ROUND(100 / (1 + EXP(AVG(pt.parametroB))), 1) AS chanceAcerto
        FROM questao q
        JOIN habilidade       h  ON q.fkHabilidade      = h.id
        JOIN areaConhecimento ac ON h.fkAreaConhecimento = ac.id
        JOIN parametroTri     pt ON q.fkParametroTri    = pt.id
        WHERE ac.sigla      = '${sigla}'
          AND pt.parametroB IS NOT NULL
          AND q.anoExame    = 2024
        GROUP BY ac.sigla, h.numero
        ORDER BY chanceAcerto DESC
        LIMIT 8;
    `;
    console.log("SQL buscarHabilidadesAcimaMedia:\n", instrucaoSql);
    return database.executar(instrucaoSql).then(resultado => {
        if (resultado.length < 7) {
            console.log(`[buscarHabilidadesAcimaMedia] Menos de 7 resultados para ${sigla}, usando fallback.`);
            return database.executar(instrucaoFallback);
        }
        return resultado;
    });
}

function buscarHabilidadesMaiorImpactoNota(sigla) {
    const instrucaoSql = `
        SELECT
            ac.sigla                                            AS area,
            h.numero                                            AS habilidade,
            COUNT(q.codigoItem)                                 AS quantidade,
            ROUND(AVG(pt.parametroA), 3)                       AS discriminacao,
            ROUND(AVG(pt.parametroB), 3)                       AS dificuldade,
            ROUND(AVG(pt.parametroC), 3)                       AS chute,
            ROUND(
                AVG(pt.parametroA) / (1 + ABS(AVG(pt.parametroB)))
            , 3)                                                AS impactoNota
        FROM questao q
        JOIN habilidade       h  ON q.fkHabilidade      = h.id
        JOIN areaConhecimento ac ON h.fkAreaConhecimento = ac.id
        JOIN parametroTri     pt ON q.fkParametroTri    = pt.id
        WHERE ac.sigla      = '${sigla}'
          AND pt.parametroA IS NOT NULL
          AND pt.parametroB IS NOT NULL
          AND q.anoExame    = 2024
        GROUP BY ac.sigla, h.numero
        ORDER BY impactoNota DESC
        LIMIT 8;
    `;
    console.log("SQL buscarHabilidadesMaiorImpactoNota:\n", instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoesPorArea() {
    var instrucaoSql = `
        SELECT
            areaConhecimento.nome  AS area,
            areaConhecimento.sigla,
            COUNT(questao.codigoItem) AS quantidade
        FROM questao
        JOIN habilidade       ON questao.fkHabilidade      = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        GROUP BY areaConhecimento.id, areaConhecimento.nome, areaConhecimento.sigla
        ORDER BY areaConhecimento.id;
    `;
    console.log("SQL buscarQuestoesPorArea:\n", instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoesPorNivel(sigla) {
    const filtroArea = sigla && sigla !== 'todos'
        ? `AND ac.sigla = '${sigla}'`
        : '';

    const instrucaoSql = `
        SELECT
            q.anoExame,
            ac.sigla    AS area,
            h.numero    AS habilidade,
            COUNT(q.codigoItem) AS quantidade
        FROM questao q
        JOIN habilidade       h  ON q.fkHabilidade      = h.id
        JOIN areaConhecimento ac ON h.fkAreaConhecimento = ac.id
        WHERE q.anoExame IN (2020, 2021, 2022, 2023, 2024)
          ${filtroArea}
        GROUP BY q.anoExame, ac.sigla, h.numero
        ORDER BY q.anoExame, ac.sigla, h.numero;
    `;
    console.log('SQL buscarQuestoesPorNivel:\n', instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarHabilidadesFrequentes() {
    var instrucaoSql = `
        SELECT
            areaConhecimento.sigla        AS area,
            habilidade.numero             AS habilidade,
            parametroTri.nivel            AS dificuldade,
            COUNT(questao.codigoItem)     AS quantidade
        FROM questao
        JOIN habilidade       ON questao.fkHabilidade      = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri     ON questao.fkParametroTri    = parametroTri.id
        GROUP BY areaConhecimento.sigla, habilidade.numero, parametroTri.nivel
        ORDER BY quantidade DESC
        LIMIT 10;
    `;
    console.log("SQL buscarHabilidadesFrequentes:\n", instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoes() {
    var instrucaoSql = `
        SELECT
            questao.codigoItem,
            areaConhecimento.sigla  AS area,
            habilidade.numero       AS habilidade,
            parametroTri.nivel      AS dificuldade,
            municipio.nome          AS municipio,
            ROUND(
                CASE areaConhecimento.sigla
                    WHEN 'MT' THEN notaMunicipal.matematica
                    WHEN 'LC' THEN notaMunicipal.codigosELinguagens
                    WHEN 'CN' THEN notaMunicipal.cienciasDaNatureza
                    WHEN 'CH' THEN notaMunicipal.cienciasHumanas
                END, 2
            ) AS notaMedia
        FROM questao
        JOIN habilidade       ON questao.fkHabilidade      = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri     ON questao.fkParametroTri    = parametroTri.id
        JOIN municipio
        JOIN notaMunicipal    ON municipio.fkNotaMunicipal  = notaMunicipal.id
        ORDER BY questao.codigoItem
        LIMIT 100;
    `;
    console.log("SQL buscarQuestoes:\n", instrucaoSql);
    return database.executar(instrucaoSql);
}

// ── EXPORTS ───────────────────────────────────────────────────────────────────
module.exports = {
    buscarNotasMunicipais,
    buscarEvolucaoNotas,
    buscarHabilidadesAbaixoMedia,
    buscarHabilidadesAcimaMedia,
    buscarHabilidadesMaiorImpactoNota,  // ← estava faltando
    buscarQuestoesPorArea,
    buscarQuestoesPorNivel,
    buscarHabilidadesFrequentes,
    buscarQuestoes,
};