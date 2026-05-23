var database = require("../database/config");

function buscarNotasMunicipais() {
    var instrucaoSql = `
        SELECT
            municipio.id AS idMunicipio,
            municipio.nome AS municipio,
            municipio.estado,
            notaMunicipal.matematica,
            notaMunicipal.codigosELinguagens,
            notaMunicipal.cienciasDaNatureza,
            notaMunicipal.cienciasHumanas
        FROM municipio
        JOIN notaMunicipal ON municipio.fkNotaMunicipal = notaMunicipal.id
        ORDER BY notaMunicipal.id DESC
        LIMIT 1;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarEvolucaoNotas() {
    var instrucaoSql = `
        SELECT
            notaMunicipal.id,
            municipio.nome AS municipio,
            notaMunicipal.matematica,
            notaMunicipal.codigosELinguagens,
            notaMunicipal.cienciasDaNatureza,
            notaMunicipal.cienciasHumanas
        FROM notaMunicipal
        LEFT JOIN municipio ON municipio.fkNotaMunicipal = notaMunicipal.id
        ORDER BY notaMunicipal.id;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
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
        WHERE ac.sigla       = '${sigla}'
          AND pt.parametroB  IS NOT NULL
        GROUP BY ac.sigla, h.numero
        HAVING chanceAcerto < 30
        ORDER BY chanceAcerto ASC
        LIMIT 8;
    `;
    console.log('SQL buscarHabilidadesAbaixoMedia:\n', instrucaoSql);
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
        GROUP BY ac.sigla, h.numero
        ORDER BY chanceAcerto DESC
        LIMIT 8;
    `;

    return database.executar(instrucaoSql)
        .then(resultado => {
            if (resultado.length < 7) {
                console.log(`[buscarHabilidadesAcimaMedia] Menos de 7 resultados para ${sigla}, usando fallback.`);
                return database.executar(instrucaoFallback);
            }
            return resultado;
        });
}

function buscarNotasHabilidades(sigla) {
    var campoNota = {
        LC: "codigosELinguagens",
        MT: "matematica",
        CN: "cienciasDaNatureza",
        CH: "cienciasHumanas"
    }[sigla];

    var instrucaoSql = `
        SELECT
            areaConhecimento.sigla AS area,
            habilidade.numero AS habilidade,
            ROUND(AVG(notaMunicipal.${campoNota}), 2) AS notaMedia,
            COUNT(questao.codigoItem) AS quantidade
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        JOIN municipio
        JOIN notaMunicipal ON municipio.fkNotaMunicipal = notaMunicipal.id
        WHERE areaConhecimento.sigla = '${sigla}'
        GROUP BY areaConhecimento.sigla, habilidade.numero
        ORDER BY notaMedia ASC, quantidade DESC
        LIMIT 10;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoesPorArea() {
    var instrucaoSql = `
        SELECT
            areaConhecimento.nome AS area,
            areaConhecimento.sigla,
            COUNT(questao.codigoItem) AS quantidade
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        GROUP BY areaConhecimento.id, areaConhecimento.nome, areaConhecimento.sigla
        ORDER BY areaConhecimento.id;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoesPorNivel(sigla) {
    var filtroArea = sigla && sigla !== "todos" ? `WHERE areaConhecimento.sigla = '${sigla}'` : "";

    var instrucaoSql = `
        SELECT
            questao.anoExame,
            parametroTri.nivel,
            COUNT(questao.codigoItem) AS quantidade
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        ${filtroArea}
        GROUP BY questao.anoExame, parametroTri.nivel
        ORDER BY questao.anoExame, parametroTri.nivel;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarHabilidadesFrequentes() {
    var instrucaoSql = `
        SELECT
            areaConhecimento.sigla AS area,
            habilidade.numero AS habilidade,
            parametroTri.nivel AS dificuldade,
            COUNT(questao.codigoItem) AS quantidade
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        GROUP BY areaConhecimento.sigla, habilidade.numero, parametroTri.nivel
        ORDER BY quantidade DESC
        LIMIT 10;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoes() {
    var instrucaoSql = `
        SELECT
            questao.codigoItem,
            areaConhecimento.sigla AS area,
            habilidade.numero AS habilidade,
            parametroTri.nivel AS dificuldade,
            municipio.nome AS municipio,
            ROUND(
                CASE areaConhecimento.sigla
                    WHEN 'MT' THEN notaMunicipal.matematica
                    WHEN 'LC' THEN notaMunicipal.codigosELinguagens
                    WHEN 'CN' THEN notaMunicipal.cienciasDaNatureza
                    WHEN 'CH' THEN notaMunicipal.cienciasHumanas
                END,
                2
            ) AS notaMedia
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        JOIN municipio
        JOIN notaMunicipal ON municipio.fkNotaMunicipal = notaMunicipal.id
        ORDER BY questao.codigoItem
        LIMIT 100;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
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
