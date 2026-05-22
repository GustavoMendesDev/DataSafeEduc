// src/routes/duckIa.js
const express = require("express");
const router = express.Router();
const duckIaController = require("../controllers/duckIaController");

router.post("/perguntar", duckIaController.responder);

module.exports = router;