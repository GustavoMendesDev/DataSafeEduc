const https = require("https");

function normalizarCanal(canalSlack) {
    var canal = String(canalSlack || "").trim();
    var partesUrlSlack = canal.match(/\/client\/[^/]+\/([^/?#]+)/);

    if (partesUrlSlack) {
        return partesUrlSlack[1];
    }

    return canal;
}

function postJson(url, corpo, token) {
    return new Promise(function (resolve, reject) {
        var payload = JSON.stringify(corpo);
        var request = https.request(url, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json; charset=utf-8",
                "Content-Length": Buffer.byteLength(payload)
            }
        }, function (response) {
            var resposta = "";

            response.on("data", function (chunk) {
                resposta += chunk;
            });

            response.on("end", function () {
                try {
                    resolve(JSON.parse(resposta));
                } catch (erro) {
                    reject(new Error("Resposta inválida da API do Slack."));
                }
            });
        });

        request.on("error", reject);
        request.write(payload);
        request.end();
    });
}

function enviarMensagem(canalSlack, mensagem) {
    var token = process.env.SLACK_BOT_TOKEN;
    var canal = normalizarCanal(canalSlack || process.env.SLACK_CHANNEL_ID);

    if (!token || token.trim() == "") {
        return Promise.reject(new Error("Configure SLACK_BOT_TOKEN no arquivo .env."));
    }

    if (!canal) {
        return Promise.reject(new Error("Informe o canal do Slack."));
    }

    if (!mensagem || String(mensagem).trim() == "") {
        return Promise.reject(new Error("Informe a mensagem do Slack."));
    }

    return postJson("https://slack.com/api/chat.postMessage", {
        channel: canal,
        text: mensagem
    }, token).then(function (respostaSlack) {
        if (!respostaSlack.ok) {
            throw new Error("Erro do Slack: " + mensagemErroSlack(respostaSlack.error));
        }

        return respostaSlack;
    });
}

function mensagemErroSlack(erroSlack) {
    if (erroSlack == "account_inactive") {
        return "account_inactive. O token configurado pertence a um app/bot ou workspace inativo. Gere um novo Bot User OAuth Token no Slack, instale o app no workspace correto e atualize SLACK_BOT_TOKEN no .env.";
    }

    if (erroSlack == "invalid_auth" || erroSlack == "not_authed") {
        return erroSlack + ". Configure SLACK_BOT_TOKEN com um Bot User OAuth Token valido, iniciado por xoxb-.";
    }

    return erroSlack;
}

module.exports = {
    enviarMensagem,
    normalizarCanal,
    mensagemErroSlack
};
