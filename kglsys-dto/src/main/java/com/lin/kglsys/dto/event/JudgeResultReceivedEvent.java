package com.lin.kglsys.dto.event;

import com.lin.kglsys.dto.mq.JudgeResultDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JudgeResultReceivedEvent extends ApplicationEvent {

    private final JudgeResultDTO resultDTO;

    /**
     * @param source    事件源，通常是发布事件的对象
     * @param resultDTO 携带的数据
     */
    public JudgeResultReceivedEvent(Object source, JudgeResultDTO resultDTO) {
        super(source);
        this.resultDTO = resultDTO;
    }
}