package com.exam.model1.lantripApply;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LanTripApplyDAO {

	@Autowired
	private SqlSession sqlSession;

	// writer - dao 통과 안해도됨
	public void lanTripApplyWrite() {
	}

	// writer_ok - flag 값있어야함
	public int lanTripApplyWriteOk(LanTripApplyTO to) {
		int flag = 1;

		int result = sqlSession.insert("lanTrip_apply_write_ok", to);
		if (result == 1) {
			flag = 0;
		}
		return flag;
	}

	// list
	   public LanTripApplyListTO boardList(LanTripApplyListTO listTO) {

	      int cpage = listTO.getCpage();
	      int recordPerPage = listTO.getRecordPerPage();
	      int blockPerPage = listTO.getBlockPerPage();
	      
	      // LanTripApplyTO의 내용을 배열로 가져옴 										mapper의 id, SqlMapper 변수명(?)과 동일
	      ArrayList<LanTripApplyTO> lists = (ArrayList)sqlSession.selectList("LanTripApplyList");
	      
	      listTO.setTotalRecord(lists.size());
	      listTO.setTotalPage(((listTO.getTotalRecord() - 1) / recordPerPage) + 1);

	      int skip = (cpage - 1) * recordPerPage;
	      
	      ArrayList<LanTripApplyTO> boardLists = new ArrayList();
	      
	      int cnt = 0;
	      for (int i = skip; i < lists.size(); i++) {
	         if(cnt == recordPerPage) {
	            break;
	         }
	         if (lists.get(i) != null) {
	        	 LanTripApplyTO to = lists.get(i);
	            boardLists.add(to);
	         }
	         cnt++;
	      }

	      listTO.setBoardList(boardLists);

	      listTO.setStartBlock(((cpage - 1) / blockPerPage) * blockPerPage + 1);
	      listTO.setEndBlock(((cpage - 1) / blockPerPage) * blockPerPage + blockPerPage);
	      if (listTO.getEndBlock() >= listTO.getTotalPage()) {
	         listTO.setEndBlock(listTO.getTotalPage());
	      }

	      return listTO;
	   }
	   
	// view
	public LanTripApplyTO lanTrip_apply_View(LanTripApplyTO to) {
		sqlSession.update("lanTrip_apply_view_hit", to);
		to = sqlSession.selectOne("lanTrip_apply_view", to);

		return to;
	}

	// delete
	public LanTripApplyTO boardDelete(LanTripApplyTO to) {
		LanTripApplyTO board = sqlSession.selectOne("delete", to);

		return board;
	}

	// delete_ok
	public int lanTrip_apply_delete_ok(LanTripApplyTO to) {
		int flag = 2;

		int result = sqlSession.delete("lanTrip_apply_delete_ok", to);
		if (result == 1) {
			flag = 0;
		} else if (result == 0) {
			flag = 1;
		}

		return flag;
	}

	// modify
	public LanTripApplyTO lanTrip_apply_modify(LanTripApplyTO to) {
		to = sqlSession.selectOne("lanTrip_apply_modify", to);

		return to;
	}

	// modify_ok
	public int lanTrip_apply_modify_ok(LanTripApplyTO to) {
		int flag = 2;
		int result = sqlSession.update("lanTrip_apply_modify_ok", to);
		if (result == 1) {
			flag = 0;
		} else if (result == 0) {
			flag = 1;
		}

		return flag;
	}
}
