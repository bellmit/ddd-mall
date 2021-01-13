package com.tactbug.mall.order.saga;

import com.tactbug.mall.order.saga.definition.SagaDefinition;
import lombok.Data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@Data
public class SagaDefinitions {

    private SagaState sagaState;
    private LinkedList<SagaDefinition> definitions = new LinkedList<>();
    private Stack<SagaDefinition> compensated = new Stack<>();
    private boolean isBuilt = false;

    public static SagaDefinitions create(SagaState sagaState){
        SagaDefinitions sagaDefinitions = new SagaDefinitions();
        sagaDefinitions.setSagaState(sagaState);
        return sagaDefinitions;
    }

    public SagaDefinitions setup(SagaDefinition definition){
        definitions.add(definition);
        return this;
    }

    public void build(){
        SagaType tag = null;
        List<SagaDefinition> copyDefinitions = Collections.synchronizedList(definitions);
        for (SagaDefinition d :
                copyDefinitions) {
            if (null == tag){
                tag = d.getSagaType();
                continue;
            }
            if (tag.equals(SagaType.DECISIVE) || tag.equals(SagaType.MUST_BE_SUCCESS)){
                if (!d.getSagaType().equals(SagaType.MUST_BE_SUCCESS)){
                    throw new TactSagaException("saga编排顺序异常: [" + d + "]");
                }
            }
            tag = d.getSagaType();
        }
        isBuilt = true;
    }

    public boolean isFinished(){
        return sagaState.getStatus().equals(SagaStatus.FINISHED);
    }

    public SagaDefinition next(){
        if (isFinished()){
            throw new TactSagaException("Saga已执行完成!");
        }
        build();
        SagaStatus status = sagaState.getStatus();
        if (status.equals(SagaStatus.EXECUTE)){
            return definitions.pollFirst();
        }
        if (status.equals(SagaStatus.COMPENSATED)){
            return compensated.pop();
        }
        throw new TactSagaException("Saga执行状态异常: [" + status + "]");
    }
}
