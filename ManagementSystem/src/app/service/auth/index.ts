import { StorageKeys } from '../storage';

export function getJwtToken(): string | null {
    return window.localStorage.getItem(StorageKeys.JWT_TOKEN);
}

export { AuthService } from './auth.service';

