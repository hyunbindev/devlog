let posts =[];
let page = 0;

let searchCondition;

function getPage(page ,board , keyword){
	const params = {page:page,board:board,keyword:keyword};
	axios.get('/api/post',
		{
			params:params
		}
	).then((res)=>{
		setPosts(res.data.content , params);
		console.log(res.data);
		const pageNumber =res.data.pageable.pageNumber;
		const totalPages=res.data.totalPages;
		setPage(pageNumber ,totalPages,params);
	})
	.catch(err=>console.log(err));
}

function setPage(pageNumber,totalPages,params){
	const pageContainer = document.getElementById('page_container');
	pageContainer.innerHTML = '';
	for(let i =0; i< totalPages; i++){
		const pageElement = document.createElement('span');
		
		pageElement.textContent = i+1;
		
		if(pageNumber == i){
			pageElement.style.fontWeight = 'bold';
		}else{
			pageElement.addEventListener('click',()=>{getPage(i , params.board , params.keyword)})
		}
		pageContainer.appendChild(pageElement);
	}
}

function deletePost(postId ,params){
	if (!window.confirm("한번 삭제하면 복구되지 않습니다. 정말로 삭제하시겠습니까?")) {
	    return;
	};
	axios.post('/api/post/delete',{id:postId})
	.then(res=>{
		console.log(res);
		getPage(params.page,params.board,params.keyword);
	})
	.catch(err=>{console.log(err)});
}

function setPosts(posts ,params){
	const postList = document.getElementById('post_list');
	postList.innerHTML = '';
	post = posts;
	post.forEach((post)=>{
		const element = document.createElement('div');
		element.classList.add('post_element');
		//포스트 정보
		const post_info = document.createElement('div');
		
		post_info.classList.add('post_info');
		const post_title = document.createElement('a');
		
		const titleHref = `/journal/${post.writer.id}/${post.id}`;
		
		post_title.href = titleHref;
		post_title.classList.add('post_title');
		post_title.textContent = post.title;
		
		const post_detail = document.createElement('div');
		post_detail.classList.add('post_detail');
		
		const post_board = document.createElement('div');
		post_board.classList.add('post_board');
		post_board.textContent =post.board.title;
		
		const post_date = document.createElement('div');
		post_date.classList.add('post_date');
		const date = new Date(post.createDate);
		const formattedDate = date.toISOString().slice(0, 16).replace('T', ' ');
		post_date.textContent = formattedDate;
		
		post_detail.appendChild(post_board);
		post_detail.appendChild(post_date);
		
		post_info.appendChild(post_title);
		post_info.appendChild(post_detail);
		
		element.appendChild(post_info);
		
		//포스트 관리 버튼
		const postBtnContainer = document.createElement('div');
		postBtnContainer.classList.add('post_btn_container');
		const deleteBtn = document.createElement('button');
		deleteBtn.textContent = '삭제';
		
		deleteBtn.addEventListener('click',()=>{deletePost(post.id,params)});
		
		const updateBtn = document.createElement('button');
		updateBtn.textContent = '수정';
		updateBtn.addEventListener('click',()=>{window.location.href = `/manage/updatepost/${post.id}`;})
		
		postBtnContainer.appendChild(deleteBtn);
		postBtnContainer.appendChild(updateBtn);
		
		element.appendChild(postBtnContainer);
		
		postList.appendChild(element);
	})
}
document.addEventListener("DOMContentLoaded", function() {
	getPage(page);
	const searchBtn = document.getElementById('search_btn');
	searchBtn.addEventListener('click',()=>{
		const board = document.getElementById('boardDrop').value;
		const keyWord = document.getElementById('keyword').value;
		if(board === 'all'){
			getPage(0,null,keyWord);
			return;
		}
		getPage(0 , board , keyWord);
	})
});
