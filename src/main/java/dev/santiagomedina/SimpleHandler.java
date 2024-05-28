package dev.santiagomedina;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;

public class SimpleHandler implements RequestHandler<String, List<Integer>> {
    @Override
    public List<Integer> handleRequest(String input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Function '" + context.getFunctionName() + "' called");

        int rolls = Integer.parseInt(input);
        List<Integer> result = new Dice(1, 6).rollTheDice(rolls);

        logger.log("Anonymous player is rolling the dice: " + result);

        return result;
    }
}
