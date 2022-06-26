# Wishlist

### Reference Documentation

The application is designed to save a list of gifts, with the option to share it with friends.

[**`WishList app`**](https://rumalwhishlist.herokuapp.com/)

### API

<details><summary><b>User registration:</b></summary><blockquote>


<details><summary>Http:</summary>

```http request
URI: http://localhost:8080/api/v1/user/registration
Method: POST
Content-Type: application/json
Accept: application/json
Authorization: none
```

</details>

<details><summary>Request:</summary>

```json
{
  "name": "User name",
  "email": "email@email.com",
  "password": "password"
}
```

</details>

<details>
  <summary>Response:</summary>

```json
{
  "id": "916211038312373297",
  "email": "email@email.com",
  "name": "User name",
  "picture": "data:image/png;base64,{...}",
  "authType": "APPLICATION",
  "role": "USER"
}
```

</details>
</blockquote></details>

<details><summary><b>User login:</b></summary><blockquote>


<details><summary>Http:</summary>

```http request
URI: http://localhost:8080/api/v1/auth/login
Method: POST
Content-Type: application/x-www-form-urlencoded
Accept: application/json
Authorization: none
Cookie: JSESSIONID
```

</details>

<details><summary>Request:</summary>

```json
{
  "email": "email@email.com",
  "password": "password"
}
```

</details>

<details>
  <summary>Response:</summary>

```json
{
  "id": "916211038312373297",
  "email": "email@email.com",
  "name": "User name",
  "picture": "data:image/png;base64,{...}",
  "authType": "APPLICATION",
  "role": "USER"
}
```

</details>
</blockquote></details>

<details><summary><b>User info:</b></summary><blockquote>


<details><summary>Http:</summary>

```http request
URI: http://localhost:8080/api/v1/user
Method: GET
Accept: application/json
```

</details>

<details>
  <summary>Response:</summary>

```json
{
  "id": "916211038312373297",
  "email": "email@email.com",
  "name": "User name",
  "picture": "data:image/png;base64,{...}",
  "authType": "APPLICATION",
  "role": "USER"
}
```

</details>
</blockquote></details>

<details><summary><b>User delete:</b></summary><blockquote>

<details><summary>Http:</summary>

```http request
URI: http://localhost:8080/api/v1/user
Method: DELETE
Accept: application/json
```

</details>

<details>
  <summary>Response:</summary>

```json
916211038312373297
```

</details>
</blockquote></details>