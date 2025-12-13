document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById("addForm");

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const formData = new FormData(form);
        const jsonData = Object.fromEntries(formData.entries());

        fetch('/expense/insert', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(jsonData)
        })
        .then(response => response.json())
        .then(data => {
            if(data.result === "ok"){
                alert('New expense added successfully');
                form.reset();
                window.location.href="/expense/view?page=1";
            } else {
                alert('Error while inserting the new expense');
            }
        })
        .catch(err => console.error(err));
    });
});