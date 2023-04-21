import {CityDto} from "./city.dto";

export interface PageDto {
  content: CityDto[];
  totalElements: number;
  totalPages: number;
}
