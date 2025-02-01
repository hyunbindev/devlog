const preElements = document.querySelectorAll("pre");
preElements.forEach((block)=>{hljs.highlightElement(block);})

const indexBlock = document.querySelectorAll("blockquote");
const toast = document.getElementById("toast");
indexBlock.forEach((index ,i) => {
	
    const href = document.createElement('div');  // <a> 태그 생성
	href.textContent = `${i+1}. ${index.innerText}`;
	href.addEventListener('click', function() {
	    let location = index.offsetTop;
		window.scrollTo({top:location, behavior:'smooth'});
	});
    toast.appendChild(href);  // 생성한 <a> 태그를 문서에 추가
});

const deletePost = (postId) => {
    
    if (!window.confirm("한번 삭제하면 복구되지 않습니다. 정말로 삭제하시겠습니까?")) {
        return;
    }
	console.log(postId);
    axios.post('/api/post/delete', { id: postId })
        .then(res => {
            alert("삭제되었습니다.");
        })
        .catch(err => {
            console.error(err);
            alert("삭제에 실패했습니다.");
        });
};

const updatePost =(postId) =>{
	window.location.href = `/manage/updatepost/${postId}`;
}
