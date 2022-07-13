package com.github.rere950303.methodsecurity.aspect;

import com.github.rere950303.methodsecurity.annotation.PreAuthorize;
import com.github.rere950303.methodsecurity.exception.AccessDeniedException;
import com.github.rere950303.methodsecurity.exception.SpELExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;

@Aspect
@Slf4j
public class AccessCheckAspect {

    private static final ExpressionParser expressionParser = new SpelExpressionParser();

    @Before("@annotation(annotation)")
    public void preAuthorize(JoinPoint joinPoint, PreAuthorize annotation) {
        Expression expression = expressionParser.parseExpression(annotation.value());
        StandardEvaluationContext evaluationContext = getEvaluationContext(joinPoint, annotation.value());
        boolean canAccess = expression.getValue(evaluationContext, Boolean.class);

        if (!canAccess) {
            throw new AccessDeniedException();
        }
    }

    private StandardEvaluationContext getEvaluationContext(JoinPoint joinPoint, String SpEL) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        setVariables(evaluationContext, joinPoint, SpEL);

        return evaluationContext;
    }

    private void setVariables(StandardEvaluationContext evaluationContext, JoinPoint joinPoint, String SpEL) {
        int variableCount = SpEL.length() - SpEL.replace("#", "").length();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        HashMap<String, Object> map = new HashMap<>();

        for (int i = 0; i < variableCount; i++) {
            String parameterName = parameterNames[i];
            Object arg = args[i];

            if (!isValidSpEL(SpEL, parameterName)) {
                throw new SpELExpressionException();
            }
            map.put(parameterName, arg);
        }

        evaluationContext.setVariables(map);
    }

    private boolean isValidSpEL(String SpEL, String parameterName) {
        int index = SpEL.indexOf(parameterName);

        return index > 0 && SpEL.charAt(index - 1) == '#';
    }
}
