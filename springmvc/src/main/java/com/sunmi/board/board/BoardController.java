package com.sunmi.board.board;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BoardController {

    BoardRepository boardRepository = new BoardRepository();

    @PostConstruct
    public void init() {
        boardRepository.save(new Board("제목1", "내용1"));
        boardRepository.save(new Board("제목2", "내용2"));
    }

    @GetMapping("/board")
    public String boardList(Model model) {
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "boardList";
    }

    @GetMapping("/add")
    public String addForm() {
        return "addForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Board board, RedirectAttributes redirectAttributes) {
        boardRepository.save(board);
        redirectAttributes.addAttribute("boardId" ,board.getId());
        return "redirect:/board/{boardId}";
    }

    @GetMapping("/board/{boardId}")
    public String board(@PathVariable int boardId, Model model) {
        Board board = boardRepository.findById(boardId);
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/edit/{boardId}")
    public String editForm(@PathVariable int boardId, Model model) {
        Board board = boardRepository.findById(boardId);
        model.addAttribute("board", board);
        return "editForm";
    }

    @PostMapping("edit/{boardId}")
    public String edit(@PathVariable int boardId, @ModelAttribute Board board) {
        boardRepository.update(boardId, board);
        return "redirect:/board/{boardId}";
    }
}
