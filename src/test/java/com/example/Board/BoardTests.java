package com.example.Board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Board.entity.Board;
import com.example.Board.entity.BoardRepository;

@SpringBootTest
public class BoardTests {

	@Autowired
	private BoardRepository boardRepository;
	
	@Test
	void save() {
		
		Board params = Board.builder()
				.title("1번 게시글 제목")
				.content("1번 게시글 내용")
				.writer("1번 게시글 작성자")
				.hits(0)
				.deleteYn('N')
				.build();

		boardRepository.save(params);
		
		Board entity = boardRepository.findById((long) 445).get();
        assertThat(entity.getTitle()).isEqualTo("1번 게시글 제목");
        assertThat(entity.getContent()).isEqualTo("1번 게시글 내용");
        assertThat(entity.getWriter()).isEqualTo("도뎡이");
	}
}
