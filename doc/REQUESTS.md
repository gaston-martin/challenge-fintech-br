# Sample requests to exercise the system

## Health check

`curl --location 'localhost:8080/health'`

## List all known countries

`curl --location localhost:8080/countries`

## List all known currencies

`curl --location localhost:8080/currencies`

## Create Wallet

### Request
```
curl --location 'localhost:8080/wallets' \
--header 'Content-Type: application/json' \
--data '{
    "userId": "someUser",
    "country": "ARGENTINA",
    "currency": "ARS"
}'
```

### Response

```
{
    "id": 1,
    "userId": "someUser",
    "currency": "ARS",
    "country": "ARGENTINA"
}
```

* **UserId** is a free text representing the ID of the user owning the wallet. This is supposed to exist in some other external system.
* **Country** is the full country name of the wallet. As listed by the `/countries` endpoint
* **Currency** is the currency code of the wallet. As listed by the `/currencies` endpoint

## Get all Wallets for a user 

```
curl --location 'localhost:8080/wallets?userId=someUser'
```

The response is a List of wallets, which might have different currencies or countries

```
[
    {
        "id": 1,
        "userId": "someUser",
        "currency": "ARS",
        "country": "ARGENTINA"
    }
]
```

## Get current balance of a wallet

```
curl --location 'localhost:8080/wallets/1/balance'
```

Requires the ID of a wallet as returned by the wallet creation endpoint

## Get balance of a wallet at some point in time

```
curl --location 'localhost:8080/wallets/1/balance/at/2025-04-30T22:30:00'
```

Requires:

* The ID of a wallet as returned by the wallet creation endpoint
* A String representation in ISO 8601 standard format for the chosen point in time (local date and time, without timezone)

This is an expensive endpoint as it needs to sum up the amounts of all movements for a given walletId.

## Deposit money

```
curl --location 'localhost:8080/wallets/1/deposit' \
--header 'Content-Type: application/json' \
--data '{
"amount": 100,
"currency": "ARS",
"reference": "Deposit from ACH 12345"
}'
```

- Adds money to a wallet as stated in the amount property.
- The endpoint would fail if the amount is negative.
- The endpoint would fail if the currency specified differs from the wallet's currency
- The reference field is optional.

## Withdraw money

```
curl --location 'localhost:8080/wallets/1/withdraw' \
--header 'Content-Type: application/json' \
--data '{
"amount": 5,
"currency": "ARS",
"reference": "withdraw to account 123456"
}'
```

- Subtracts money from a wallet according to the specified amount.
- The amount is expected (and validated) to be positive.
- The endpoint would fail if the currency specified differs from the wallet's currency
- The reference field is optional.

## Transfer money

```
curl --location 'localhost:8080/transfer' \
--header 'Content-Type: application/json' \
--data '{
"amount": 10,
"currency": "ARS",
"payerWalletId": 1,
"collectorWalletId": 2,
"reference": "Money transfer 123456"
}
'
```

- Transfer money from one wallet to another
- The wallet sending the money is specified by its ID as payerWalletId
- The wallet receiving the money is specified by its ID as collectorWalletId
- Both sending and receiving wallets must have the same currency
- The currency of the transaction must match the currency of the wallets
- The reference property is optional and a free text.