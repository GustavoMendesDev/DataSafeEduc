var database = require("../database/config")

<<<<<<< HEAD
function autenticar(nome, senha) {
    console.log("ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function entrar(): ", nome, senha)
    var instrucaoSql = `
        SELECT nome, senha FROM usuario WHERE nome = '${nome}' AND senha = '${senha}';
    `;
=======
function escapar(valor) {
    return String(valor).replace(/\\/g, "\\\\").replace(/'/g, "\\'");
}

function autenticar(email, senha) {
    console.log("ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function entrar(): ", email, senha)
    
    var instrucaoSql = `
        SELECT
            usuario.id AS idUsuario,
            usuario.nome,
            usuario.email,
            usuario.senha,
            usuario.fkNivelAcesso,
            nivelAcesso.nome AS nivelAcesso,
            usuario.fkCursinho,
            cursinho.nome AS nomeCursinho
        FROM usuario
        JOIN nivelAcesso ON usuario.fkNivelAcesso = nivelAcesso.id
        JOIN cursinho ON usuario.fkCursinho = cursinho.id
        WHERE usuario.email = '${email}' 
          AND usuario.senha = '${senha}';
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function registrarAcesso(idUsuario, ip) {
    var instrucaoSql = `
        INSERT INTO logAcesso (ip, dataCriacao, fkUsuario)
        VALUES ('${escapar(ip)}', NOW(), ${Number(idUsuario)});
    `;

>>>>>>> dashboard
    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

// Coloque os mesmos parâmetros aqui. Vá para a var instrucaoSql
<<<<<<< HEAD
function cadastrar(nome,senha) {
    console.log("ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function cadastrar():", nome, senha);
    
    // Insira exatamente a query do banco aqui, lembrando da nomenclatura exata nos valores
    //  e na ordem de inserção dos dados.
    var instrucaoSql = `
        INSERT INTO usuario (nome, senha) VALUES ('${nome}', '${senha}');
    `;
=======
async function cadastrar(nome,email,senha,codigoConvite) {
    console.log("ACESSEI O USUARIO MODEL \n \n\t\t >> Se aqui der erro de 'Error: connect ECONNREFUSED',\n \t\t >> verifique suas credenciais de acesso ao banco\n \t\t >> e se o servidor de seu BD está rodando corretamente. \n\n function cadastrar():", nome, email,senha);
    
 var buscarCurso = `SELECT id FROM cursinho WHERE codigoConvite = '${codigoConvite}';`;

    try {
        // 2. Executa a busca usando o seu objeto 'database.executar'
        // Na sua estrutura, esse método já retorna o array de resultados do banco
        const resultadoBusca = await database.executar(buscarCurso);

        // 3. Verifica se o banco encontrou o cursinho
        if (resultadoBusca.length > 0) {
            // Pega o primeiro objeto retornado do array
            const primeiroCursinho = resultadoBusca[0]; 
            
            // Captura o ID do cursinho encontrado (ajuste se a coluna tiver outro nome no banco, ex: idCursinho)
            var idCursinho = primeiroCursinho.id; 
            
            console.log(`Cursinho encontrado! ID: ${idCursinho}`);

            // 4. Cria a instrução de INSERT utilizando o idCursinho que acabamos de achar
            var instrucaoSql = `
                INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, email, fkCursinho)
                VALUES (
                    '${nome}',
                    '${senha}',
                    NOW(),
                    (SELECT id FROM nivelAcesso ORDER BY id LIMIT 1),
                    '${email}',
                    ${idCursinho}
                );
            `;

            console.log("Executando a instrução SQL de Cadastro: \n" + instrucaoSql);
            
            // Executa e retorna o resultado do cadastro
            return await database.executar(instrucaoSql);

        } else {
            console.log("Nenhum cursinho encontrado com o código de convite fornecido.");
            // Lança um erro ou retorna uma mensagem para tratar no seu controller
            throw new Error("Código de convite inválido.");
        }

    } catch (erro) {
        console.error("Erro no processo de cadastro:", erro);
        throw erro; // Repassa o erro para o controller tratar a resposta HTTP
    }
}

function cadastrarCoordenadores(nomeCursinho, email, senha, confirmarSenha){
    var instrucaoSql = `Select * from usuarios`;
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

function cadastrarCompleto(nome, senha, fkNivelAcesso, fkCursinho, email) {
    var instrucaoSql = `
        INSERT INTO usuario (nome,  senha, dataCriacao, fkNivelAcesso, fkCursinho,  'email')
        VALUES ('${nome}', '${senha}', NOW(), ${fkNivelAcesso}, ${fkCursinho}, ${ElementInternals});
    `;

    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

function atualizar(idUsuario, nome, email, senha, fkNivelAcesso, fkCursinho) {
    var campos = [`nome = '${nome}'`];

    if (email != undefined && email != "") {
        campos.push(`email = '${email}'`);
    }

    if (senha != undefined && senha != "") {
        campos.push(`senha = '${senha}'`);
    }

    if (fkNivelAcesso != undefined) {
        campos.push(`fkNivelAcesso = ${fkNivelAcesso}`);
    }

    if (fkCursinho != undefined) {
        campos.push(`fkCursinho = ${fkCursinho}`);
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

>>>>>>> dashboard
    console.log("Executando a instrução SQL: \n" + instrucaoSql);
    return database.executar(instrucaoSql);
}

module.exports = {
    autenticar,
<<<<<<< HEAD
    cadastrar
};
=======
    registrarAcesso,
    cadastrar,
    listarPorNivel,
    cadastrarCompleto,
    atualizar,
    excluir
};
>>>>>>> dashboard
