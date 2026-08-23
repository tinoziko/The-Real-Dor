package com.tinoziko.the_real_dor.the_real_dor;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class PotyPredictionService {

    private OrtEnvironment env;
    private OrtSession session;

    @PostConstruct
    public void init() throws Exception {
        env = OrtEnvironment.getEnvironment();
        try (InputStream modelStream = getClass().getResourceAsStream("/poty_model.onnx")) {
            if (modelStream == null) {
                throw new RuntimeException("Cannot find poty_model.onnx in resources!");
            }
            byte[] modelBytes = modelStream.readAllBytes();
            session = env.createSession(modelBytes, new OrtSession.SessionOptions());
        }
    }

    public double predictScore(PlayerStats stats) throws Exception {

        // what the ai sees
        System.out.println("\n=== FEEDING AI: " + stats.getName() + " ===");
        System.out.println("Minutes: " + stats.getTotalMinutes() + " | Pos: " + stats.getPosition());
        System.out.println("Goals: " + stats.getGoals() + " | Assists: " + stats.getAssists());
        System.out.println("Goals p90: " + stats.getGoalsPer90() + " | Ast p90: " + stats.getAssistsPer90());
        System.out.println("xG: " + stats.getExpectedGoals() + " | SCA p90: " + stats.getScaPer90());
        System.out.println("KeyPass p90: " + stats.getKeyPassesPer90() + " | Int p90: " + stats.getInterceptionsPer90());
        System.out.println("TklWon p90: " + stats.getTacklesWonPer90() + " | ProgPass p90: " + stats.getProgPassesPer90());
        System.out.println("ProgCarry p90: " + stats.getProgCarriesPer90());
        System.out.println("======================================");


        Map<String, OnnxTensor> inputs = new HashMap<>();

        inputs.put("Total_Minutes", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getTotalMinutes() }}));
        inputs.put("Goals", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getGoals() }}));
        inputs.put("Assists", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getAssists() }}));
        inputs.put("Goals_p_90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getGoalsPer90() }}));
        inputs.put("Assists_p_90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getAssistsPer90() }}));
        inputs.put("Expected_Goals", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getExpectedGoals() }}));
        inputs.put("Shot_creating_actions_p_90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getScaPer90() }}));
        inputs.put("Key_Passes_p90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getKeyPassesPer90() }}));
        inputs.put("Interceptions_p90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getInterceptionsPer90() }}));
        inputs.put("Tackles_Won_p90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getTacklesWonPer90() }}));
        inputs.put("Prog_Passes_p90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getProgPassesPer90() }}));
        inputs.put("Prog_Carries_p90", OnnxTensor.createTensor(env, new float[][]{{ (float) stats.getProgCarriesPer90() }}));
        inputs.put("pos", OnnxTensor.createTensor(env, new String[][]{{ stats.getPosition() }}));

        try (OrtSession.Result results = session.run(inputs)) {
            float[][] output = (float[][]) results.get(0).getValue();
            return output[0][0];
        } finally {
            for (OnnxTensor tensor : inputs.values()) {
                tensor.close();
            }
        }
    }
}