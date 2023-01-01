/*
 */
package com.ppac37.EText2022;

/**
 *
 * @author q6
 */
public class ForumUneEntreeConcours extends ForumUneDef implements Comparable<Object> {

    public ForumUneEntreeConcours(ForumUneDef d) {
        super(d);
    }

    
    @Override
    public int compareTo(Object o) {
        // pour trier dans l'order des gagnats ?
         if (o == null) {
            return -1;
        } else {
            if (o instanceof ForumUneDef) {
                ForumUneDef d = (ForumUneDef) o;
                // TODO affinier avec la date de creation et de modification pour comparaison d'historique de commentaire 
                if ( d.getReactionsTotals() == this.getReactionsTotals()){
                    // ? 
                    return d.commentDateCreation.compareTo(this.commentDateCreation);
                }else{
                return d.getReactionsTotals() - this.getReactionsTotals();
                }

            } else {
                // TODO
                System.err.printf("TODO UnDef comparaTo class %s\n", o.getClass().getSimpleName());
                return -1;
            }
        }
    }
    
    
}
