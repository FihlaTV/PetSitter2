package com.zekisanmobile.petsitter2.event;

import com.zekisanmobile.petsitter2.vo.Sitter;

import java.util.List;

public class UpdateSittersEvent {

    private List<Sitter> sitters;

    public UpdateSittersEvent(List<Sitter> sitters) {
        this.sitters = sitters;
    }

    public List<Sitter> getSitters() {
        return sitters;
    }

    public void setSitters(List<Sitter> sitters) {
        this.sitters = sitters;
    }
}
