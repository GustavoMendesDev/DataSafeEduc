var usuarioModel = require("../models/usuarioModel");

function autenticar(req, res) {
    var email = req.body.emailServer;
    var senha = req.body.senhaServer;

    if (email == undefined) {
        res.status(400).send("Seu email está undefined");
    } else if (senha == undefined) {
        res.status(400).send("Sua senha está indefinida!");
    } else {

        usuarioModel
            .autenticar(email, senha)
            .then(function (resultadoAutenticar) {
                    console.log(`\nResultados encontrados: ${resultadoAutenticar.length}`);
                    console.log(`Resultados: ${JSON.stringify(resultadoAutenticar)}`); // transforma JSON em String

                    if (resultadoAutenticar.length == 1) {
                        console.log(resultadoAutenticar);

                        var usuarioAutenticado = resultadoAutenticar[0];
                        var ip = req.headers["x-forwarded-for"] || req.socket.remoteAddress || req.ip || "";

                        usuarioModel.registrarAcesso(usuarioAutenticado.idUsuario, ip)
                            .then(function () {
                                res.json({
                                    id: usuarioAutenticado.idUsuario,
                                    idUsuario: usuarioAutenticado.idUsuario,
                                    nome: usuarioAutenticado.nome,
                                    email: usuarioAutenticado.email
                                });
                            }).catch(function (erro) {
                                console.log(erro);
                                console.log("\nHouve um erro ao registrar o acesso! Erro: ", erro.sqlMessage);
                                res.status(500).json(erro.sqlMessage);
                            });
                    } else if (resultadoAutenticar.length == 0) {
                        res.status(403).send("Senha inválido(s)");
                    } else {
                        res.status(403).send("Mais de um usuário com o mesmo login e senha!");
                    }
                }
            ).catch(
                function (erro) {
                    console.log(erro);
                    console.log("\nHouve um erro ao realizar o login! Erro: ", erro.sqlMessage);
                    res.status(500).json(erro.sqlMessage);
                }
            );
    }

}

function cadastrar(req, res) {
    // Crie uma variável que vá recuperar os valores do arquivo cadastro.html
    var nome = req.body.nomeServer;
    var email = req.body.emailServer;
    var senha = req.body.senhaServer;

    // Faça as validações dos valores
    if (nome == undefined) {
        res.status(400).send("Seu nome está undefined!");
    }else if (email == undefined) {
        res.status(400).send("Seu email está undefined!");
    } else if (senha == undefined) {
        res.status(400).send("Sua senha está undefined!");
    } else {
        // Passe os valores como parâmetro e vá para o arquivo usuarioModel.js
        usuarioModel
        .cadastrar(nome, email, senha)
            .then(
                function (resultado) {
                    res.json(resultado);
                }
            ).catch(
                function (erro) {
                    console.log(erro);
                    console.log(
                        "\nHouve um erro ao realizar o cadastro! Erro: ",
                        erro.sqlMessage
                    );
                    res.status(500).json(erro.sqlMessage);
                }
            );
    }
}

function cadastrarCoordenador(res, res){
    var nomeCursinho = req.body.nomeCursinho;
    var email = req.body.email;
    var senha = req.body.senha;
    var confirmarSenha = req.body.confirmarSenha;

    if(nomeCursinho == undefined){
        res.status(400).send("Nome cursinho undefined!");
    }else if(email == undefined){
        res.status(400).send("Seu email está undefined");
    }else if(senha == undefined){
        res.status(400).send("Sua senha está undefined!");
    }else if(confirmarSenha == undefined){
        res.status(400).send("Digite a senha igual ao campo anterior")
    }else{
         // Passe os valores como parâmetro e vá para o arquivo usuarioModel.js
                coordenadorModel.cadastrar(nomeCursinho, email, senha, confirmarSenha)
                    .then(
                        function (resultado) {
                            res.json(resultado);
                        }
                    ).catch(
                        function (erro) {
                            console.log(erro);
                            console.log(
                                "\nHouve um erro ao realizar o cadastro! Erro: ",
                                erro.sqlMessage
                            );
                            res.status(500).json(erro.sqlMessage);
                        }
                    );
    }
}

function editarCoordenadores(){
    
}


function listarPorNivel(req, res) {
    var nivel = req.params.nivel;

    if (nivel == undefined) {
        res.status(400).send("Nível de acesso está undefined!");
    } else {
        usuarioModel.listarPorNivel(nivel)
            .then(function (resultado) {
                res.status(200).json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function cadastrarCompleto(req, res) {
    var nome = req.body.nomeServer || req.body.nome;
    var email = req.body.emailServer || req.body.email;
    var senha = req.body.senhaServer || req.body.senha;
    var fkNivelAcesso = req.body.fkNivelAcesso;
    var fkMunicipio = req.body.fkMunicipio;

    if (nome == undefined) {
        res.status(400).send("Seu nome está undefined!");
    } else if (email == undefined) {
        res.status(400).send("Seu email está undefined!");
    } else if (senha == undefined) {
        res.status(400).send("Sua senha está undefined!");
    } else if (fkNivelAcesso == undefined) {
        res.status(400).send("Seu nível de acesso está undefined!");
    } else if (fkMunicipio == undefined) {
        res.status(400).send("Seu município está undefined!");
    } else {
        usuarioModel.cadastrarCompleto(nome, senha, fkNivelAcesso, fkMunicipio, email)
            .then(function (resultado) {
                res.status(201).json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function atualizar(req, res) {
    var idUsuario = Number(req.params.id);
    var nome = req.body.nomeServer || req.body.nome;
    var senha = req.body.senhaServer || req.body.senha;
    var fkNivelAcesso = req.body.fkNivelAcesso;
    var fkMunicipio = req.body.fkMunicipio;

    if (!idUsuario) {
        res.status(400).send("ID do usuário inválido!");
    } else if (nome == undefined) {
        res.status(400).send("Seu nome está undefined!");
    } else {
        usuarioModel.atualizar(idUsuario, nome, senha, fkNivelAcesso, fkMunicipio)
            .then(function (resultado) {
                res.status(200).json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

function excluir(req, res) {
    var idUsuario = Number(req.params.id);

    if (!idUsuario) {
        res.status(400).send("ID do usuário inválido!");
    } else {
        usuarioModel.excluir(idUsuario)
            .then(function (resultado) {
                res.status(200).json(resultado);
            }).catch(function (erro) {
                console.log(erro);
                res.status(500).json(erro.sqlMessage);
            });
    }
}

module.exports = {
    autenticar,
    cadastrar,
    listarPorNivel,
    cadastrarCompleto,
    atualizar,
    excluir
};
