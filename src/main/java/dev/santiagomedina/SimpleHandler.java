package dev.santiagomedina;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

import java.util.List;

public class SimpleHandler implements RequestHandler<String, List<Integer>> {

    private final OpenTelemetry openTelemetry = buildOpenTelemetry();
    private final Tracer tracer = openTelemetry.getTracer(SimpleHandler.class.getName(), "0.1.0");
    @Override
    public List<Integer> handleRequest(String input, Context context) {
        Span span = tracer.spanBuilder("rollTheDice").startSpan();
        try (Scope scope = span.makeCurrent()) {
            LambdaLogger logger = context.getLogger();
            logger.log("Function '" + context.getFunctionName() + "' called");

            int rolls = Integer.parseInt(input);
            List<Integer> result = new Dice(1, 6).rollTheDice(rolls);

            logger.log("Anonymous player is rolling the dice: " + result);

            return result;
        } catch(Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    private OpenTelemetry buildOpenTelemetry() {
        return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    }
}
