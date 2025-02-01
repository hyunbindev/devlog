let chart;
window.addEventListener('DOMContentLoaded', (event) => {
	const ctx = document.getElementById('chart').getContext('2d');
	chart = initChart(ctx);
	initChartData();
	getTotalVisitor();
	getVisitorHistory();
});

function initChart(ctx){
	return chart = new Chart(ctx, {
		type:'line',
		data:{
			labels:[],
			datasets:[{
				label:'방문자',
				data:[],
				borderColor:'rgba(75, 192, 192, 1)',
				backgroundColor: 'rgba(75, 192, 192, 0.2)',
				fill: true,
			}]
		},
		options:{
			responsive:true,
			plugins:{
				legend:{
					position:'none',
				}
			}
		}
	})
}

function initChartData() {
    let days = [];
    let today = new Date();  // 현재 날짜

    // 30일 전까지 날짜를 구함
    for (let i = 29; i >= 0; i--) {
        // 오늘 날짜에서 i일을 빼서 새로운 날짜를 만듬
        let date = new Date(today);
        date.setDate(today.getDate() - (29 - i));  // 30에서 i를 빼면 오늘부터 30일 전까지
        days.push(date.getDate());  // 날짜를 'YYYY-MM-DD' 형식으로 저장
    }

    console.log(days);  // 확인을 위해 콘솔에 출력 (배열에 저장된 날짜들)

    // 차트의 labels에 날짜 배열을 설정
    chart.data.labels = days;
    chart.update();  // 차트를 업데이트하여 변경된 데이터를 반영
}

function getTotalVisitor(){
	axios.get('/api/statistics/visitor/total')
	.then((res)=>{
		const totalVisitor = document.getElementById('total_visitor_count');
		totalVisitor.textContent = res.data;
	})
	.catch((err)=>console.error(err));
}

function getVisitorHistory(){
	axios.get('/api/statistics/visitor/month')
	.then((res)=>{
		let historyData = [];
		let chartData = [];
		let labels=[];
		res.data.forEach((data)=>{historyData.push(data)});
		let today = new Date();
		for(let i =0; i<30; i++){
			let date = new Date(today);
			date.setDate(today.getDate() - (29 - i));
			//데이터와 날자가 일치할 시
			const compareDate = new Date(historyData[historyData.length -1].date);
			if(compareDates(date , compareDate)){
				const logData = historyData.pop();
				chartData.push(logData.visitorCount);
			}
			else{
				chartData.push(0);
			}
			//월단위 경계 데이터
			labels.push([date.getDate(),date.getMonth()+1]);
		}
		chart.data.datasets[0].data = chartData;
		chart.data.labels = labels;
		chart.update();
	})
	.catch((err)=>{console.error(err)});
}

function compareDates(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() &&
           d1.getMonth() === d2.getMonth() &&
           d1.getDate() === d2.getDate();
}