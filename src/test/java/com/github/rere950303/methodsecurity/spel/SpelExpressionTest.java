package com.github.rere950303.methodsecurity.spel;

import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.assertj.core.api.Assertions.assertThat;

@Import(SpelExpressionTest.Car.class)
public class SpelExpressionTest {

    ExpressionParser expressionParser = new SpelExpressionParser();

    @Test
    public void expression() throws Exception {
        Car car = Car.builder().yearOfProduction(1000).model("model").make("make").build();
        Expression expression = expressionParser.parseExpression("model == 'model'");

        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(car);
        boolean result = expression.getValue(evaluationContext, Boolean.class);
        assertThat(result).isTrue();
    }

    @Getter
    @Builder
    static class Car {

        public int yearOfProduction;
        private String model;
        private String make;
    }

}
