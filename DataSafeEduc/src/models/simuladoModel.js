var database = require("../database/config");

function escapar(valor) {
    return String(valor).replace(/\\/g, "\\\\").replace(/'/g, "\\'");
}

function valorSql(valor) {
    if (valor == undefined || valor == "") {
        return "NULL";
    }

    return `'${escapar(valor)}'`;
}

function filtroDificuldadeSql(dificuldade) {
    if (!dificuldade) return "";

    if (dificuldade == "Fácil") {
        return "AND parametroTri.parametroB < -0.5";
    }

    if (dificuldade == "Médio") {
        return "AND parametroTri.parametroB >= -0.5 AND parametroTri.parametroB <= 0.8";
    }

    if (dificuldade == "Difícil") {
        return "AND parametroTri.parametroB > 0.8";
    }

    return `AND parametroTri.nivel = ${valorSql(dificuldade)}`;
}

function listar() {
    var instrucaoSql = `
        SELECT
            simulado.id,
            simulado.nomeSimulado,
            simulado.quantidadeQuestoes,
            usuario.nome AS usuario,
            GROUP_CONCAT(DISTINCT areaConhecimento.nome ORDER BY areaConhecimento.nome SEPARATOR ', ') AS tema,
            GROUP_CONCAT(DISTINCT habilidade.numero ORDER BY habilidade.numero SEPARATOR ', ') AS habilidades,
            GROUP_CONCAT(DISTINCT parametroTri.nivel ORDER BY parametroTri.nivel SEPARATOR ', ') AS dificuldade
        FROM simulado
        LEFT JOIN usuario ON simulado.fkUsuario = usuario.id
        LEFT JOIN questaoSimulado ON simulado.id = questaoSimulado.fkSimulado
        LEFT JOIN questao ON questaoSimulado.fkQuestao = questao.codigoItem
        LEFT JOIN habilidade ON questao.fkHabilidade = habilidade.id
        LEFT JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        LEFT JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        GROUP BY simulado.id, simulado.nomeSimulado, simulado.quantidadeQuestoes, usuario.nome
        ORDER BY simulado.id DESC;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function detalhar(idSimulado) {
    var instrucaoSql = `
        SELECT
            simulado.id,
            simulado.nomeSimulado,
            simulado.quantidadeQuestoes,
            areaConhecimento.nome AS area,
            areaConhecimento.sigla,
            habilidade.numero AS habilidade,
            questao.codigoItem,
            questao.anoExame,
            parametroTri.nivel AS dificuldade,
            parametroTri.parametroA,
            parametroTri.parametroB,
            parametroTri.parametroC
        FROM simulado
        JOIN questaoSimulado ON simulado.id = questaoSimulado.fkSimulado
        JOIN questao ON questaoSimulado.fkQuestao = questao.codigoItem
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        WHERE simulado.id = ${idSimulado}
        ORDER BY questao.codigoItem;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarQuestoesParaSimulado(sigla, dificuldade, habilidades, quantidade) {
    var filtroArea = sigla ? `AND areaConhecimento.sigla = ${valorSql(sigla)}` : "";
    var filtroDificuldade = filtroDificuldadeSql(dificuldade);
    var filtroHabilidades = habilidades && habilidades.length > 0
        ? `AND habilidade.numero IN (${habilidades.map(function (item) { return valorSql(item); }).join(",")})`
        : "";

    var instrucaoSql = `
        SELECT questao.codigoItem
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        WHERE 1 = 1
            ${filtroArea}
            ${filtroDificuldade}
            ${filtroHabilidades}
        ORDER BY RAND()
        LIMIT ${quantidade};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function listarQuestoesDisponiveis(sigla, dificuldade, habilidades, idSimulado, codigoAtual) {
    var filtroArea = sigla ? `AND areaConhecimento.sigla = ${valorSql(sigla)}` : "";
    var filtroDificuldade = filtroDificuldadeSql(dificuldade);
    var filtroHabilidades = habilidades && habilidades.length > 0
        ? `AND habilidade.numero IN (${habilidades.map(function (item) { return valorSql(item); }).join(",")})`
        : "";
    var filtroSimulado = idSimulado
        ? `AND (
            questao.codigoItem NOT IN (
                SELECT fkQuestao FROM questaoSimulado WHERE fkSimulado = ${Number(idSimulado)}
            )
            ${codigoAtual ? `OR questao.codigoItem = ${valorSql(codigoAtual)}` : ""}
        )`
        : "";

    var instrucaoSql = `
        SELECT
            questao.codigoItem,
            questao.anoExame,
            areaConhecimento.nome AS area,
            areaConhecimento.sigla,
            habilidade.numero AS habilidade,
            parametroTri.nivel AS dificuldade,
            parametroTri.parametroA,
            parametroTri.parametroB,
            parametroTri.parametroC
        FROM questao
        JOIN habilidade ON questao.fkHabilidade = habilidade.id
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        JOIN parametroTri ON questao.fkParametroTri = parametroTri.id
        WHERE 1 = 1
            ${filtroArea}
            ${filtroDificuldade}
            ${filtroHabilidades}
            ${filtroSimulado}
        ORDER BY questao.anoExame DESC, questao.codigoItem
        LIMIT 50;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function criar(nomeSimulado, quantidadeQuestoes, fkUsuario) {
    var instrucaoSql = `
        INSERT INTO simulado (nomeSimulado, quantidadeQuestoes, fkUsuario)
        VALUES (${valorSql(nomeSimulado)}, ${quantidadeQuestoes}, ${fkUsuario});
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function vincularQuestoes(idSimulado, questoes) {
    var valores = questoes.map(function (questao) {
        var codigoItem = typeof questao == "string" ? questao : questao.codigoItem;
        return `(${valorSql(codigoItem)}, ${idSimulado})`;
    }).join(",");

    var instrucaoSql = `
        INSERT INTO questaoSimulado (fkQuestao, fkSimulado)
        VALUES ${valores};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function atualizarQuantidade(idSimulado, quantidadeQuestoes) {
    var instrucaoSql = `
        UPDATE simulado
        SET quantidadeQuestoes = ${Number(quantidadeQuestoes)}
        WHERE id = ${Number(idSimulado)};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function excluirQuestoes(idSimulado) {
    var instrucaoSql = `
        DELETE FROM questaoSimulado WHERE fkSimulado = ${idSimulado};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function excluir(idSimulado) {
    var instrucaoSql = `
        DELETE FROM simulado WHERE id = ${idSimulado};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function listarHabilidades(sigla) {
    var filtroArea = sigla ? `WHERE areaConhecimento.sigla = '${sigla}'` : "";

    var instrucaoSql = `
        SELECT
            areaConhecimento.sigla AS area,
            habilidade.numero,
            habilidade.descricao
        FROM habilidade
        JOIN areaConhecimento ON habilidade.fkAreaConhecimento = areaConhecimento.id
        ${filtroArea}
        ORDER BY areaConhecimento.id, habilidade.numero;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

module.exports = {
    listar,
    detalhar,
    buscarQuestoesParaSimulado,
    listarQuestoesDisponiveis,
    criar,
    vincularQuestoes,
    atualizarQuantidade,
    excluirQuestoes,
    excluir,
    listarHabilidades
};
