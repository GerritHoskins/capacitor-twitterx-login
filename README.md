# @gerrithoskins/capacitor-twitterx-login

Allows quick and easy authentication with Twitter/X

## Install

```bash
npm install @gerrithoskins/capacitor-twitterx-login
npx cap sync
```

## API

<docgen-index>

* [`login()`](#login)
* [`logout()`](#logout)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### login()

```typescript
login() => Promise<TwitterXLoginResponse>
```

**Returns:** <code>Promise&lt;<a href="#twitterxloginresponse">TwitterXLoginResponse</a>&gt;</code>

--------------------


### logout()

```typescript
logout() => Promise<void>
```

--------------------


### Interfaces


#### TwitterXLoginResponse

| Prop              | Type                |
| ----------------- | ------------------- |
| **`accessToken`** | <code>string</code> |
| **`userName`**    | <code>string</code> |
| **`userId`**      | <code>string</code> |

</docgen-api>