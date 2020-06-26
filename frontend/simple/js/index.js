$(document).ready(function () {

    const uploadUrl = 'http://localhost:8080/upload'

    $('#uploadButton').click(() => {
        const form = new FormData($('#uploadForm')[0])

        $.ajax({
            type: "POST",
            url: uploadUrl,
            data: form,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            success: response => {
                alert(response)
            },
            error: xhr => {
                alert("something went wrong")
                console.log(xhr)
            }
        })
    })

})