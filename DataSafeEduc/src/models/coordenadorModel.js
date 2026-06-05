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

function nivelCoordenadorSql() {
    return "(SELECT id FROM nivelAcesso WHERE nome LIKE '%Coordenador%' LIMIT 1)";
}

function municipioSql(fkMunicipio, nomeCursinho) {
    if (fkMunicipio != undefined && fkMunicipio != "") {
        return Number(fkMunicipio);
    }

    if (nomeCursinho != undefined && nomeCursinho != "") {
        return `COALESCE(
            (SELECT id FROM municipio WHERE nome = ${valorSql(nomeCursinho)} LIMIT 1),
            (SELECT id FROM municipio ORDER BY id LIMIT 1)
        )`;
    }

    return "(SELECT id FROM municipio ORDER BY id LIMIT 1)";
}

function cursinhoSql(fkCursinho, nomeCursinho) {
    if (fkCursinho != undefined && fkCursinho != "") {
        return Number(fkCursinho);
    }

    if (nomeCursinho != undefined && nomeCursinho != "") {
        return `COALESCE(
            (SELECT id FROM cursinho WHERE id = ${valorSql(nomeCursinho)} LIMIT 1),
            (SELECT id FROM cursinho ORDER BY id LIMIT 1)
        )`;
    }

    return "(SELECT id FROM cursinho ORDER BY id LIMIT 1)";
}

function listar() {
    var instrucaoSql = `
        SELECT
            usuario.id,
            usuario.nome,
            usuario.nome AS email,
            usuario.senha,
            usuario.dataCriacao,
            usuario.fkNivelAcesso,
            nivelAcesso.nome AS cargo,
            usuario.fkCursinho,
            cursinho.nome AS nomeCursinho
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        JOIN cursinho ON usuario.fkCursinho = cursinho.id
        WHERE nivelAcesso.nome LIKE '%Coordenador%'
        ORDER BY usuario.id;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarPorId(idCoordenador) {
    var instrucaoSql = `
        SELECT
            usuario.id,
            usuario.nome,
            usuario.nome AS email,
            usuario.senha,
            usuario.dataCriacao,
            usuario.fkNivelAcesso,
            nivelAcesso.nome AS cargo,
            usuario.fkCursinho,
            cursinho.nome AS nomeCursinho
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        JOIN cursinho ON usuario.fkCursinho = cursinho.id
        WHERE usuario.id = ${Number(idCoordenador)}
            AND nivelAcesso.nome LIKE '%Coordenador%';
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function cadastrar(nome, senha, nomeCursinho, fkCursinho) {
    var instrucaoSql = `
        INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho)
        VALUES (
            ${valorSql(nome)},
            ${valorSql(senha)},
            NOW(),
            ${nivelCoordenadorSql()},
            ${cursinhoSql(fkCursinho, nomeCursinho)}
        );
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function atualizar(idCoordenador, nome, senha, fkCursinho) {
    var campos = [`nome = ${valorSql(nome)}`];

    if (senha != undefined && senha != "") {
        campos.push(`senha = ${valorSql(senha)}`);
    }

    if ((fkCursinho != undefined && fkCursinho != "")) {
        campos.push(`fkCursinho = ${cursinhoSql(fkCursinho)}`);
    }

    var instrucaoSql = `
        UPDATE usuario
        SET ${campos.join(", ")}
        WHERE id = ${Number(idCoordenador)}
            AND fkNivelAcesso = ${nivelCoordenadorSql()};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function excluir(idCoordenador) {
    var instrucaoSql = `
        DELETE FROM usuario
        WHERE id = ${Number(idCoordenador)}
            AND fkNivelAcesso = ${nivelCoordenadorSql()};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

module.exports = {
    listar,
    buscarPorId,
    cadastrar,
    atualizar,
    excluir
};
