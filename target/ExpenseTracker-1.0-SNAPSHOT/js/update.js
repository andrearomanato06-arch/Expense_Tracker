document.addEventListener('DOMContentLoaded', function(){

    const form = document.getElementById("update_form");

    form.addEventListener('submit', function(event){

        event.preventDefault();

        const formData = new FormData(form);

        updateExpense(
            formData.get('id'),
            formData.get('purchaseDate'),
            formData.get('description'),
            formData.get('amount'),
            formData.get('category')
        );
    });
});

function updateExpense(id, purchaseDate, description, amount, category){

    const parameters = new URLSearchParams({
        id: id,
        purchaseDate: purchaseDate,
        description: description,
        amount: amount,
        category: category
    });

    fetch(`/expense/update?${parameters.toString()}`,{
        method: 'PUT'
    })
    .then(response => {
        if(response.ok){
            alert(`Expense with ID: ${id} updated successfully!`);
            window.location.href = '/expense/view?page=1';
        }else{
            alert(`An error occoured while updating expense with ID: ${id}`);
        }
    }).catch(error => console.error(error));
}