var database = require("../database/config")

function autenticar(nome, senha) {
    console.log("ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function entrar(): ", nome, senha)
    var instrucaoSql = `
        SELECT
            usuario.id,
            usuario.nome,
            usuario.senha,
            usuario.fkNivelAcesso,
            nivelAcesso.nome AS nivelAcesso,
            usuario.fkMunicipio
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        WHERE usuario.nome = '${nome}' AND usuario.senha = '${senha}';
    `;
    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

// Coloque os mesmos parâmetros aqui. Vá para a var instrucaoSql
function cadastrar(nome,senha) {
    console.log("ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function cadastrar():", nome, senha);
    
    // Insira exatamente a query do banco aqui, lembrando da nomenclatura exata nos valores
    //  e na ordem de inserção dos dados.
    var instrucaoSql = `
        INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkMunicipio)
        VALUES (
            '${nome}',
            '${senha}',
            NOW(),
            (SELECT id FROM nivelAcesso ORDER BY id LIMIT 1),
            (SELECT id FROM municipio ORDER BY id LIMIT 1)
        );
    `;
    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function listarPorNivel(nivel) {
    var instrucaoSql = `
        SELECT
            usuario.id,
            usuario.nome,
            usuario.senha,
            usuario.dataCriacao,
            nivelAcesso.nome AS cargo,
            municipio.nome AS municipio
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        JOIN municipio ON usuario.fkMunicipio = municipio.id
        WHERE nivelAcesso.nome LIKE '%${nivel}%'
        ORDER BY usuario.id;
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function cadastrarCompleto(nome, senha, fkNivelAcesso, fkMunicipio) {
    var instrucaoSql = `
        INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkMunicipio)
        VALUES ('${nome}', '${senha}', NOW(), ${fkNivelAcesso}, ${fkMunicipio});
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function atualizar(idUsuario, nome, senha, fkNivelAcesso, fkMunicipio) {
    var campos = [`nome = '${nome}'`];

    if (senha != undefined && senha != "") {
        campos.push(`senha = '${senha}'`);
    }

    if (fkNivelAcesso != undefined) {
        campos.push(`fkNivelAcesso = ${fkNivelAcesso}`);
    }

    if (fkMunicipio != undefined) {
        campos.push(`fkMunicipio = ${fkMunicipio}`);
    }

    var instrucaoSql = `
        UPDATE usuario SET ${campos.join(", ")}
        WHERE id = ${idUsuario};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function excluir(idUsuario) {
    var instrucaoSql = `
        DELETE FROM usuario WHERE id = ${idUsuario};
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

module.exports = {
    autenticar,
    cadastrar,
    listarPorNivel,
    cadastrarCompleto,
    atualizar,
    excluir
};
