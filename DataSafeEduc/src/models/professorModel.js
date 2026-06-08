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

// Nova função apontando especificamente para o nível de Professor
function nivelProfessorSql() {
    return "(SELECT id FROM nivelAcesso WHERE nome LIKE '%Professor%' LIMIT 1)";
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
            (SELECT id FROM cursinho WHERE nome = ${valorSql(nomeCursinho)} LIMIT 1),
            (SELECT id FROM cursinho ORDER BY id LIMIT 1)
        )`;
    }

    return "(SELECT id FROM cursinho ORDER BY id LIMIT 1)";
}

function listar(fkCursinho) {
    var instrucaoSql = `
        SELECT
            usuario.id,
            usuario.nome,
            usuario.email,
            usuario.senha,
            usuario.dataCriacao,
            usuario.fkNivelAcesso,
            nivelAcesso.nome AS cargo,
            usuario.fkCursinho,
            cursinho.nome AS nomeCursinho
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        JOIN cursinho ON usuario.fkCursinho = cursinho.id
        WHERE nivelAcesso.nome LIKE '%Professor%'
            AND usuario.fkCursinho = ${Number(fkCursinho)}
        ORDER BY usuario.id;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function buscarPorId(idProfessor) {
    var instrucaoSql = `
        SELECT
            usuario.id,
            usuario.nome,
            usuario.email,
            usuario.senha,
            usuario.dataCriacao,
            usuario.fkNivelAcesso,
            nivelAcesso.nome AS cargo,
            usuario.fkCursinho,
            cursinho.nome AS nomeCursinho
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        JOIN cursinho ON usuario.fkCursinho = cursinho.id
        WHERE usuario.id = ${Number(idProfessor)}
            AND nivelAcesso.nome LIKE '%Professor%';
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function cadastrar(nome, email, senha, fkCursinho) {
    var instrucaoSql = `
        INSERT INTO usuario (nome, email, senha, dataCriacao, fkNivelAcesso, fkCursinho)
        VALUES (
            ${valorSql(nome)},
            ${valorSql(email)},
            ${valorSql(senha)},
            NOW(),
            ${nivelProfessorSql()},
            ${Number(fkCursinho)}
        );
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function atualizar(idProfessor, nome, email, senha, fkCursinho) {
    // Adicionando NOME e EMAIL no array de atualização
    var campos = [
        `nome = ${valorSql(nome)}`,
    ];

    if (email != undefined && email != "") {
        campos.push(`email = ${valorSql(email)}`);
    }

    if (senha != undefined && senha != "") {
        campos.push(`senha = ${valorSql(senha)}`);
    }

    if (fkCursinho != undefined && fkCursinho != "") {
        campos.push(`fkCursinho = ${Number(fkCursinho)}`);
    }

    var instrucaoSql = `
        UPDATE usuario
        SET ${campos.join(", ")}
        WHERE id = ${Number(idProfessor)}
            AND fkNivelAcesso = ${nivelProfessorSql()};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function excluir(idProfessor) {
    var instrucaoSql = `
        DELETE FROM usuario
        WHERE id = ${Number(idProfessor)}
            AND fkNivelAcesso = ${nivelProfessorSql()};
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

