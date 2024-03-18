package com.sunmi.board.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Board {

    private int id;
    private String title;
    private String context;

    public Board(String title, String context) {
        this.title = title;
        this.context = context;
    }
}
