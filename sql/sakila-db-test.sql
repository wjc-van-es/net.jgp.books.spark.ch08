select * 
from nicer_but_slower_film_list fl 
where 1 = 1
and fl.rating = 'PG'
order by fl.category;

select * from actor
order by last_name;

select actor.first_name, actor.last_name, film.title
 from actor
 inner join film_actor
 on actor.actor_id = film_actor.actor_id
 inner join film
 on film_actor.film_id = film.film_id
 where actor.last_name = 'WINSLET'
 and film.rating = 'NC-17'
 ;