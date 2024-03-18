package com.sunmi.board.board;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardRepository {

    private static final Map<Integer, Board> boardMap = new HashMap<>();

    private static int sequence = 0;

    public void save(Board board) {
        board.setId(++sequence);
        boardMap.put(board.getId(), board);
    }

    public List<Board> findAll() {
        return new ArrayList<>(boardMap.values());
    }

    public Board findById(int id) {
        return boardMap.get(id);
    }

    public void update(int id, Board updateBoard) {
        Board board = findById(id);
        board.setTitle(updateBoard.getTitle());
        board.setContext(updateBoard.getContext());
    }

}
